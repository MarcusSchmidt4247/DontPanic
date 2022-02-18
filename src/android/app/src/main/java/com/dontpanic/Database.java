// MS: 2/4/22 - began work on Database class
// MS: 2/7/22 - improved class safety
// MS: 2/9/22 - added return values, static list of module names, and new functions
// MS: 2/10/22 - reworked how the database connection is handled, added new functions
// MS: 2/17/22 - converted to Android specific libraries, added database creation
// MS: 2/18/22 - create functions now return the ID of the new entity rather than a boolean

package com.dontpanic;

// SQLite imports
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.*;
import android.util.Log;

// Miscellaneous imports
import java.io.File;
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
       to be called when the app or activity is done. Can't be done in the destructor because there's
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
                Log.e("Database Error", e.getMessage());
                return false;
            }
        }
        
        return true;
    }

    /* Returns whether or not a database exists on this device. If not, create one.
       This function is intended to be called immediately when the app launches. */
    public static boolean DatabaseExists(Context context)
    {
        // Save the absolute path to the database for use when we no longer have the application context
        ABSOLUTE_DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getAbsolutePath();

        // Reference: https://stackoverflow.com/questions/3386667
        File file = context.getDatabasePath(DATABASE_NAME);
        if (!file.exists())
        {
            if (DatabaseConnected())
                // If the database did not already exist but was successfully created in DatabaseConnected(), create its tables
                CreateTables();

            // Return false because the database did not already exist
            return false;
        }
        else
            // Return true because the database already existed
            return true;
    }

    // Returns the name of a module from its ID, or null if it fails
    public static String GetModuleName(int modID)
    {
        // If the list of module names haven't already been placed into a list, do so now
        if (moduleNames == null)
        {
            // Build a new list
            moduleNames = new ArrayList<>();
            try
            {
                // Query the database for its module names and add it to the list in order of their ID
                SQLiteCursor results = (SQLiteCursor) db.rawQuery("SELECT Name FROM Module ORDER BY ID ASC", null);
                while (results.moveToNext())
                    moduleNames.add(results.getString(0));
                results.close();
            }
            catch (SQLException e)
            {
                Log.e("Database Error", e.getMessage());
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

    // Returns the new user's ID, or -1 if unable to create
    public static int CreateUser(String name)
    {
        if (!DatabaseConnected())
            return -1;
        
        try
        {
            // Insert a new user with the provided name into the User table
            ContentValues cv = new ContentValues();
            cv.put("Name", name);
            db.insert("User", null, cv);

            // Query the table to retrieve the automatically assigned ID for this user and return it (or -1 if it cannot be found)
            SQLiteCursor result = (SQLiteCursor) db.rawQuery("SELECT ID FROM User WHERE Name = ?", new String[] { name });
            int id = -1;
            if (result.moveToNext())
            {
                id = result.getInt(0);
            }
            result.close();
            return id;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return -1;
        }
    }

    public static void DeleteUser()
    {

    }

    //**************************************************
    // Functions that have to do with module sequences *
    //**************************************************

    // Return the new sequence's ID, or -1 if it failed to create
    public static int CreateSequence(int usrID, String name)
    {
        if (!DatabaseConnected())
            return -1;

        try
        {
            // Insert a new sequence into the ModuleSequence table with the provided usrID and name
            ContentValues cv = new ContentValues();
            cv.put("usrID", usrID);
            cv.put("Name", name);
            db.insert("ModuleSequence", null, cv);

            // Query the table to retrieve the automatically assigned ID for this sequence and return it (or -1 if it cannot be found)
            SQLiteCursor result = (SQLiteCursor) db.rawQuery("SELECT ID FROM ModuleSequence WHERE usrID = ? AND Name = ?", new String[] { String.valueOf(usrID), name });
            int id = -1;
            if (result.moveToNext())
            {
                id = result.getInt(0);
            }
            result.close();
            return id;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return -1;
        }
    }

    // Return the success of deleting a sequence
    public static boolean DeleteSequence(int usrID, int seqID)
    {
        if (!DatabaseConnected())
            return false;

        try
        {
            db.delete("ModuleSequence", "usrID = ? AND ID = ?",
                    new String[] { String.valueOf(usrID), String.valueOf(seqID) });
            return true;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
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
            SQLiteCursor results = (SQLiteCursor) db.rawQuery("SELECT modID FROM SequenceOrder WHERE seqID = ? ORDER BY modOrder ASC", new String[] { String.valueOf(seqID) });
            while (results.moveToNext())
                sequence.add(results.getInt(results.getColumnIndex("modID")));
            results.close();
            return sequence;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return null;
        }
    }

    // Return the success of the module insertion
    public static boolean InsertModuleIntoSequence(int seqID, int modID, int index)
    {
        // Ensure that there is a valid connection to the database
        if (!DatabaseConnected())
            return false;

        // Increment index to convert from Java's 0-based index to SQLite's 1-based index
        index++;

        // If the order of all of the modules beyond the point this module should go were successfully incremented
        if (PushBackSequenceOrder(seqID, index))
        {
            // Insert the new module in the vacant spot
            try
            {
                ContentValues cv = new ContentValues();
                cv.put("seqID", seqID);
                cv.put("modID", modID);
                cv.put("modOrder", index);
                db.insert("SequenceOrder", null, cv);
            }
            catch (SQLException e)
            {
                Log.e("Database Error", e.getMessage());
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
            SQLiteCursor results = (SQLiteCursor) db.rawQuery("SELECT modOrder FROM SequenceOrder WHERE seqID = ? ORDER BY modOrder DESC LIMIT 1", new String[] { String.valueOf(seqID) } );
            // If an order was returned, set this module to have an order one greater
            if (results.moveToNext())
                order = results.getInt(0) + 1;
            // If there were no results, there are no modules in this sequence yet, so the order is 1 (as though autoincremented)
            else
                order = 1;
            results.close();

            ContentValues cv = new ContentValues();
            cv.put("seqID", seqID);
            cv.put("modID", modID);
            cv.put("modOrder", order);
            db.insert("SequenceOrder", null, cv);
            return true;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
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

    private static final String DATABASE_NAME = "app_data.db";
    private static String ABSOLUTE_DATABASE_PATH;
    private static SQLiteDatabase db = null;
    private static ArrayList<String> moduleNames = null;

    /* Return whether or not there is a valid connection to the database in the variable 'db'
       Source: https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/ */
    private static boolean DatabaseConnected()
    {
        if (db != null && db.isOpen())
            return true;

        try
        {
            db = SQLiteDatabase.openOrCreateDatabase(ABSOLUTE_DATABASE_PATH, null);
            return true;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return false;
        }
    }

    /* Given an index of 3, this function will increment the order of each module in the sequence greater than or equal to 3
       and return the success of the operation.
       NOTE: index is expected to be a 1-based */
    private static boolean PushBackSequenceOrder(int seqID, int index)
    {
        if (!DatabaseConnected())
            return false;

        try
        {
            SQLiteCursor results = (SQLiteCursor) db.rawQuery("SELECT modOrder FROM SequenceOrder WHERE seqID = ? ORDER BY modOrder DESC", new String[] { String.valueOf(seqID) });
            while(results.moveToNext())
            {
                int order = results.getInt(results.getColumnIndex("modOrder"));

                if (order >= index)
                {
                    ContentValues cv = new ContentValues();
                    cv.put("modOrder", order + 1);
                    db.update("SequenceOrder", cv, "modOrder = ?", new String[] { String.valueOf(order) });
                }
            }

            results.close();
            return true;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return false;
        }
    }

    private static void CreateTables()
    {
        if (DatabaseConnected())
        {
            try
            {
                db.execSQL("CREATE TABLE IF NOT EXISTS User (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Name VARCHAR(30) UNIQUE," +
                        "setting_1 INTEGER NOT NULL DEFAULT 0," +
                        "setting_n INTEGER NOT NULL DEFAULT 0)");

                db.execSQL("CREATE TABLE IF NOT EXISTS Module (" +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Name text)");

                // Fill the Module table with the default values (should not be user-generated)
                String[] modules = { "Meditation", "Haptics", "Exercises", "Reflection" };
                for (String module : modules)
                {
                    ContentValues cv = new ContentValues();
                    cv.put("Name", module);
                    db.insert("Module", null, cv);
                }

                db.execSQL("CREATE TABLE IF NOT EXISTS CompletedModule (" +
                        "usrID INTEGER REFERENCES User(ID) ON DELETE CASCADE," +
                        "modID INTEGER REFERENCES Module(ID)," +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Rating INTEGER," +
                        "Date TEXT NOT NULL DEFAULT CURRENT_DATE)");

                db.execSQL("CREATE TABLE IF NOT EXISTS ModuleSequence (" +
                        "usrID INTEGER REFERENCES User(ID) ON DELETE CASCADE," +
                        "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Name VARCHAR(30) UNIQUE)");

                db.execSQL("CREATE TABLE IF NOT EXISTS SequenceOrder (" +
                        "seqID INTEGER REFERENCES ModuleSequence(ID) ON DELETE CASCADE," +
                        "modID INTEGER REFERENCES Module(ID) ON DELETE CASCADE," +
                        "modOrder INTEGER NOT NULL," +
                        "PRIMARY KEY (seqID, modOrder))");
            }
            catch (SQLException e)
            {
                Log.e("Database Error", e.getMessage());
            }
        }
    }
}