// MS: 2/4/22 - began work on Database class
// MS: 2/7/22 - improved class safety

package com.dontpanic;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

/* The class is final with a private constructor and only defines static member functions in order to
   mimic a top-level static class, which for some reason is not allowed in Java. */
public final class Database
{
    private Database() { }

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

    //****************************************************
    // Functions that have to do with users and settings *
    //****************************************************

    public static void CreateUser()
    {

    }

    public static void DeleteUser()
    {

    }

    //**************************************************
    // Functions that have to do with module sequences *
    //**************************************************

    public static void CreateSequence()
    {

    }

    public static void DeleteSequence()
    {

    }

    //*********************************************************************
    // Functions that have to do with modules inside of a module sequence *
    //*********************************************************************

    public static void GetModulesInSequence(int seqID)
    {
        Connection db = GetConnection();
        if (db == null)
            return;

        try
        {
            Statement stmt = db.createStatement();
            String sql = "SELECT Name, modOrder FROM SequenceOrder JOIN Module ON modID = ID WHERE seqID = " + seqID + " ORDER BY modOrder ASC";
            ResultSet results = stmt.executeQuery(sql);
            while (results.next())
            {
                String name = results.getString("Name");
                int order = results.getInt("modOrder");

                System.out.println(name + ", " + order);
            }
            results.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void InsertModuleIntoSequence(int seqID, int modID, int index)
    {
        Connection db = GetConnection();
        if (db == null)
            return;

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
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void AppendModuleToSequence()
    {

    }

    public static void MoveModuleInSequence()
    {

    }

    public static void DeleteModuleFromSequence()
    {

    }

    //************************************************************************
    // Private functions and variables that are used by the public functions *
    //************************************************************************

    private static Connection db = null;

    /* Return the connection to the database, or create one if it doesn't exist.
       Might return null in the case of an error.
       Source: https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/ */
    private static Connection GetConnection()
    {
        if (db != null)
            return db;

        try
        {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:test.db";
            db = DriverManager.getConnection(url);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Failed to locate JDBC driver");
        }

        return db;
    }

    /* Given an index of 3, this function will increment the order of each module in the sequence greater than or equal to 3
       and return the success of the operation */
    private static boolean PushBackSequenceOrder(int seqID, int index)
    {
        Connection db = GetConnection();
        if (db == null)
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