// MS: 2/4/22 - began work on Database class
// MS: 2/7/22 - improved class safety
// MS: 2/9/22 - added return values, static list of module names, and new functions
// MS: 2/10/22 - reworked how the database connection is handled, added new functions

package com.dontpanic;

// SQLite imports
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

// Miscellaneous imports
import java.util.ArrayList;

/* The class is final with a private constructor and only defines static member functions in order to
   mimic a top-level static class, which for some reason is not allowed in Java. */
public final class Database
{
    private Database() { }

    //*****************************
    // General database functions *
    //*****************************

    /* Although the database never has to be manually initiated, this disconnect function does have
       to be called when the app is shutting down. Can't be done in the destructor because there's
       no instance to be destroyed. */
    public static boolean Disconnect()
    {
        if (db != null)
        {
            try
            {
                db.close();
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return false;
            }
        }
        
        return true;
    }

    // Returns the name of a module from its ID, or null if it fails
    public static String GetModuleName(int modID)
    {
        // If the list of module names haven't already been placed into a list, do so now
        if (moduleNames == null)
        {
            // Build a new list
            moduleNames = new ArrayList<String>();
            try
            {
                // Query the database for its module names and add it to the list in order of their ID
                Statement stmt = db.createStatement();
                ResultSet results = stmt.executeQuery("SELECT Name FROM Module ORDER BY ID ASC");
                while (results.next())
                    moduleNames.add(results.getString("Name"));
                results.close();
                stmt.close();
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
                return null;
            }
        }

        /* The index of a module's name is one less than its ID due to the disparity between SQLite autoincrement
           and this ArrayList's zero based indexing. */
        return moduleNames.get(modID - 1);
    }

    //****************************************************
    // Functions that have to do with users and settings *
    //****************************************************

    public static boolean CreateUser(String name)
    {
        if (!DatabaseConnected())
            return false;
        
        try
        {
            Statement stmt = db.createStatement();
            String sql = "INSERT INTO User (Name) VALUES ('" + name + "')";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void DeleteUser()
    {

    }

    //**************************************************
    // Functions that have to do with module sequences *
    //**************************************************

    // Return the success of creating a new sequence
    public static boolean CreateSequence(int usrID, String name)
    {
        if (!DatabaseConnected())
            return false;

        try
        {
            Statement stmt = db.createStatement();
            String sql = "INSERT INTO ModuleSequence (usrID, Name) VALUES (" + usrID + ",'" + name + "')";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Return the success of deleting a sequence
    public static boolean DeleteSequence(int usrID, int seqID)
    {
        if (!DatabaseConnected())
            return false;

        try
        {
            Statement stmt = db.createStatement();
            String sql = "DELETE FROM ModuleSequence WHERE usrID = " + usrID + " AND ID = " + seqID;
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //*********************************************************************
    // Functions that have to do with modules inside of a module sequence *
    //*********************************************************************

    // Returns a List of the sequence's module ID's in order, or null on failure
    public static ArrayList<Integer> GetModulesInSequence(int seqID)
    {
        // Ensure that there is a valid connection to the database
        if (!DatabaseConnected())
            return null;
        
        ArrayList<Integer> sequence = new ArrayList<Integer>();
        try
        {
            // Query the modID column and add each result to 'sequence'
            Statement stmt = db.createStatement();
            String sql = "SELECT modID FROM SequenceOrder WHERE seqID = " + seqID + " ORDER BY modOrder ASC";
            ResultSet results = stmt.executeQuery(sql);
            while (results.next())
                sequence.add(results.getInt("modID"));
            results.close();
            stmt.close();
            return sequence;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Return the success of the module insertion
    public static boolean InsertModuleIntoSequence(int seqID, int modID, int index)
    {
        // Ensure that there is a valid connection to the database
        if (!DatabaseConnected())
            return false;

        // If the order of all of the modules beyond the point this module should go were successfully incremented
        if (PushBackSequenceOrder(seqID, index))
        {
            // Insert the new module in the vacant spot
            try
            {
                Statement stmt = db.createStatement();
                String sql = "INSERT INTO SequenceOrder(seqID, modID, modOrder) VALUES (" + seqID + "," + modID + "," + index + ")";
                stmt.executeUpdate(sql);
                stmt.close();
                return true;
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }

        return false;
    }

    // Return the success of appending a module
    public static boolean AppendModuleToSequence(int seqID, int modID)
    {
        // Ensure that there is a valid connection to the database
        if (!DatabaseConnected())
            return false;

        try
        {
            // Find the highest order in the sequence
            int order;
            String sql = "SELECT modOrder FROM SequenceOrder WHERE seqID = " + seqID + " ORDER BY modOrder DESC LIMIT 1";
            Statement stmt = db.createStatement();
            ResultSet results = stmt.executeQuery(sql);
            // If an order was returned, set this module to have an order one greater
            if (results.next())
                order = results.getInt("modOrder") + 1;
            // If there were no results, there are no modules in this sequence yet, so the order is 1 (as though autoincremented)
            else
                order = 1;
            results.close();

            sql = "INSERT INTO SequenceOrder(seqID, modID, modOrder) VALUES (" + seqID + "," + modID + "," + order + ")";
            stmt.executeUpdate(sql);
            stmt.close();
            return true;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Return the success of moving a module from one index to another.
    public static boolean MoveModuleInSequence(int seqID, int startIndex, int endIndex)
    {
        return false;
    }

    public static void DeleteModuleFromSequence()
    {

    }

    //************************************************************************
    // Private functions and variables that are used by the public functions *
    //************************************************************************

    private static Connection db = null;
    private static ArrayList<String> moduleNames = null;

    /* Return whether or not there is a valid connection to the database in the variable 'db'
       Source: https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/ */
    private static boolean DatabaseConnected()
    {
        if (db != null)
            return true;

        try
        {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:test.db";
            db = DriverManager.getConnection(url);
            return true;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Failed to locate JDBC driver");
        }

        return false;
    }

    /* Given an index of 3, this function will increment the order of each module in the sequence greater than or equal to 3
       and return the success of the operation */
    private static boolean PushBackSequenceOrder(int seqID, int index)
    {
        if (!DatabaseConnected())
            return false;

        try
        {
            PreparedStatement sqlUpdate = db.prepareStatement("UPDATE SequenceOrder SET modOrder = ? WHERE modOrder = ?");
            
            Statement stmt = db.createStatement();
            ResultSet results = stmt.executeQuery("SELECT modOrder FROM SequenceOrder WHERE seqID = " + seqID + " ORDER BY modOrder DESC");
            while (results.next())
            {
                int order = results.getInt("modOrder");

                if (order >= index)
                {
                    sqlUpdate.setInt(1, order + 1);
                    sqlUpdate.setInt(2, order);
                    sqlUpdate.executeUpdate();
                }
            }

            return true;
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            return false;
        }
    }
}