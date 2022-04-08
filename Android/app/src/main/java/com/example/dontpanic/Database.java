// MS: 2/4/22 - began work on Database class
// MS: 2/7/22 - improved class safety
// MS: 2/9/22 - added return values, static list of module names, and new functions
// MS: 2/10/22 - reworked how the database connection is handled, added new functions
// MS: 2/17/22 - converted to Android specific libraries, added database creation
// MS: 2/18/22 - create functions now return the ID of the new entity rather than a boolean
// MS: 2/22/22 - made Module an enum and Preferences a static class with generic GetPreferences() function
// MS: 2/23/22 - added more preference handling, rewrote GetModulesInSequence() to return new type Module
// MS: 2/27/22 - a few minor improvements to safety and/or efficiency, wrote PrintTable() function for testing purposes
// MS: 2/28/22 - added two new preferences, SetPreference(), and UserExists()
// MS: 3/17/22 - added GetUserSequenceIDs() and converted GetModulesInSequence() to GetSequence() using new Sequence object

package com.example.dontpanic;

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
    public static final float DEFAULT_BREATH_DURATION = 6.5f;
    private Database() { }

    //*****************************
    // General database functions *
    //*****************************

    // For testing purposes ONLY! Otherwise, this exposes way too much data. Comment out when not needed.
    /* public static void PrintTable(String table)
    {
        if (DatabaseConnected())
        {
            try
            {
                String query = "SELECT * FROM " + table;
                SQLiteCursor results = (SQLiteCursor) db.rawQuery(query, null);
                int row = 1;
                while (results.moveToNext())
                {
                    Log.i(table, "Row " + row + " of " + results.getCount());
                    for (int i = 0; i < results.getColumnCount(); i++)
                    {
                        int type = results.getType(i);
                        if (type == Cursor.FIELD_TYPE_INTEGER)
                            Log.i(results.getColumnName(i), String.valueOf(results.getInt(i)));
                        else if (type == Cursor.FIELD_TYPE_FLOAT)
                            Log.i(results.getColumnName(i), String.valueOf(results.getFloat(i)));
                        else if (type == Cursor.FIELD_TYPE_STRING)
                            Log.i(results.getColumnName(i), results.getString(i));
                        else if (type == Cursor.FIELD_TYPE_BLOB)
                            Log.i(results.getColumnName(i), Arrays.toString(results.getBlob(i)));
                        else
                            Log.i(results.getColumnName(i), "NULL");
                    }
                    row++;
                }
                results.close();
            }
            catch (SQLException e)
            {
                Log.i("Database Error", e.getMessage());
            }
        }
    } */

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

    //**********************************************************
    // Functions to get and set the current user's preferences *
    //**********************************************************

    /* Returns the wrapper to each preference's primitive data type (e.g. Integer rather than int), or
       null in the case of failure. */
    public static Object GetPreference(String preference)
    {
        if (!DatabaseConnected() || currentUserID == -1)
            return null;

        // selectionArgs only works in the WHERE clause, so construct this query manually
        String query = "SELECT " + preference + " FROM User WHERE ID = " + currentUserID;

        try
        {
            // Check if this preference is any of the ones with integer values
            if (preference.equals(Preferences.LAUNCH_SEQUENCE_INT) || preference.equals(Preferences.BREATHING_CYCLES_INT))
            {
                // If so, query the User table for this preference and return the integer value
                SQLiteCursor result = (SQLiteCursor) db.rawQuery(query, null);
                Integer val = -1;
                if (result.moveToNext())
                {
                    val = result.getInt(0);
                }
                result.close();
                return val;
            }
            // Check if this preference is any of the ones with float values
            else if (preference.equals(Preferences.HAPTIC_STRENGTH_FLOAT) || preference.equals(Preferences.AUDIO_VOLUME_FLOAT) ||
                     preference.equals(Preferences.BREATHING_DURATION_FLOAT) || preference.equals(Preferences.TEXT_SCALING_FLOAT))
            {
                // If so, query the User table for this preference and return the float value
                SQLiteCursor result = (SQLiteCursor) db.rawQuery(query, null);
                Float val = -1.0f;
                if (result.moveToNext())
                {
                    val = result.getFloat(0);
                }
                result.close();
                return val;
            }
            // Check if this preference is any of the ones with boolean values (which are stored in the database as integers)
            else if (preference.equals(Preferences.HAPTICS_ENABLED_BOOLEAN) || preference.equals(Preferences.BREATHING_AUDIO_ENABLED_BOOLEAN) ||
                     preference.equals(Preferences.BREATHING_HAPTICS_ENABLED_BOOLEAN))
            {
                // If so, query the User table for this preference and return the boolean value
                SQLiteCursor result = (SQLiteCursor) db.rawQuery(query, null);
                Integer val = 0;
                if (result.moveToNext())
                {
                    val = result.getInt(0);
                }
                result.close();
                // Return true if the result is greater than zero (should be 1), otherwise false
                return (val > 0);
            }
            // If this preference is not a recognized value, return null
            else
                return null;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return null;
        }
    }

    public static boolean SetPreference(String preference, int value)
    {
        if (!DatabaseConnected() || currentUserID == -1)
            return false;

        // Verify this is a valid preference to receive an int
        if (preference.equals(Preferences.LAUNCH_SEQUENCE_INT) || preference.equals(Preferences.BREATHING_CYCLES_INT))
        {
            ContentValues cv = new ContentValues();
            cv.put(preference, value);
            int updatedRows = db.update("User", cv, "ID = ?", new String[] { String.valueOf(currentUserID) });
            return (updatedRows > 0);
        }
        else
            return false;
    }

    public static boolean SetPreference(String preference, float value)
    {
        if (!DatabaseConnected() || currentUserID == -1)
            return false;

        // Verify this is a valid preference to receive a float
        if (preference.equals(Preferences.HAPTIC_STRENGTH_FLOAT) || preference.equals(Preferences.AUDIO_VOLUME_FLOAT) ||
            preference.equals(Preferences.BREATHING_DURATION_FLOAT) || preference.equals(Preferences.TEXT_SCALING_FLOAT))
        {
            ContentValues cv = new ContentValues();
            cv.put(preference, value);
            int updatedRows = db.update("User", cv, "ID = ?", new String[] { String.valueOf(currentUserID) });
            return (updatedRows > 0);
        }
        else
            return false;
    }

    public static boolean SetPreference(String preference, boolean value)
    {
        if (!DatabaseConnected() || currentUserID == -1)
            return false;

        // Verify this is a valid preference to receive a boolean
        if (preference.equals(Preferences.HAPTICS_ENABLED_BOOLEAN) || preference.equals(Preferences.BREATHING_AUDIO_ENABLED_BOOLEAN) ||
            preference.equals(Preferences.BREATHING_HAPTICS_ENABLED_BOOLEAN))
        {
            ContentValues cv = new ContentValues();
            cv.put(preference, value);
            int updatedRows = db.update("User", cv, "ID = ?", new String[] { String.valueOf(currentUserID) });
            return (updatedRows > 0);
        }
        else
            return false;
    }

    //****************************************************
    // Functions that have to do with users and settings *
    //****************************************************

    // The ID of the user who is currently logged in, or -1 if no one is
    public static int currentUserID = -1;

    // Return whether or not at least one user exists in the database
    public static boolean UserExists()
    {
        if (!DatabaseConnected())
            return false;

        SQLiteCursor result = (SQLiteCursor) db.rawQuery("SELECT ID FROM User", null);
        int count = result.getCount();
        result.close();
        return (count > 0);
    }

    // Returns the new user's ID, or -1 if unable to create
    public static int CreateUser(String name)
    {
        if (!DatabaseConnected())
            return -1;
        
        try
        {
            // Insert a new user with the provided name into the User table and return its row ID (which will equal the auto-incremented ID column)
            ContentValues cv = new ContentValues();
            cv.put("Name", name);
            return (int) db.insert("User", null, cv);
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

    // Return a list of all of the current user's sequence ID's
    public static ArrayList<Integer> GetUserSequenceIDs()
    {
        // Ensure that there is a valid connection to the database
        if (!DatabaseConnected() || currentUserID == -1)
            return null;

        ArrayList<Integer> sequences = new ArrayList<>();
        try
        {
            SQLiteCursor results = (SQLiteCursor) db.rawQuery("SELECT ID FROM ModuleSequence WHERE usrID = ? ORDER BY ID ASC", new String[] { String.valueOf(currentUserID) });
            int seqID;
            while (results.moveToNext())
            {
                seqID = results.getInt(results.getColumnIndex("ID"));
                sequences.add(seqID);
            }
            results.close();
            return sequences;
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return null;
        }
    }

    // Returns a Sequence object, or null on failure
    public static Sequence GetSequence(int seqID)
    {
        // Ensure that there is a valid connection to the database
        if (!DatabaseConnected())
            return null;

        ArrayList<Module> modules = new ArrayList<>();
        try
        {
            // Query the database for the module ID's in the sequence, in order
            SQLiteCursor results = (SQLiteCursor) db.rawQuery("SELECT modID FROM SequenceOrder WHERE seqID = ? ORDER BY modOrder ASC", new String[] { String.valueOf(seqID) });
            int modID;
            while (results.moveToNext())
            {
                // Extract the ID of the next module in the sequence
                modID = results.getInt(results.getColumnIndex("modID"));
                // Then add an instance of this type of module to the sequence

                //ModuleReference addingModule = Module.GetModule(modID);       Deprecated, no need for Module Reference anymore
                modules.add(Module.GetModule(modID));
            }
            results.close();

            // Query the database for the name of the sequence
            results = (SQLiteCursor) db.rawQuery("SELECT Name FROM ModuleSequence WHERE ID = ?", new String[] { String.valueOf(seqID) });
            String name;
            if (results.moveToNext())
                name = results.getString(results.getColumnIndex("Name"));
            else
                return null;

            // Return a Sequence object instantiated with the previously found values
            return new Sequence(seqID, name, modules);
        }
        catch (SQLException e)
        {
            Log.e("Database Error", e.getMessage());
            return null;
        }
    }

    //*********************************************************************
    // Functions that have to do with modules inside of a module sequence *
    //*********************************************************************

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

    /* Return whether or not there is a valid connection to the database in the variable 'db'
       Source: https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/ */
    private static boolean DatabaseConnected()
    {
        if (db != null && db.isOpen())
            return true;

        try
        {
            db = SQLiteDatabase.openOrCreateDatabase(ABSOLUTE_DATABASE_PATH, null);
            db.execSQL("PRAGMA foreign_keys=ON");
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
                        Preferences.HAPTICS_ENABLED_BOOLEAN + " INTEGER NOT NULL DEFAULT 1," +
                        Preferences.HAPTIC_STRENGTH_FLOAT + " REAL NOT NULL DEFAULT 1.0," +
                        Preferences.AUDIO_VOLUME_FLOAT + " REAL NOT NULL DEFAULT 1.0," +
                        Preferences.LAUNCH_SEQUENCE_INT + " INTEGER DEFAULT NULL," +
                        Preferences.TEXT_SCALING_FLOAT + " INTEGER DEFAULT 1.0," +
                        Preferences.BREATHING_DURATION_FLOAT + " REAL NOT NULL DEFAULT " + DEFAULT_BREATH_DURATION + "," +
                        Preferences.BREATHING_AUDIO_ENABLED_BOOLEAN + " INTEGER NOT NULL DEFAULT 0," +
                        Preferences.BREATHING_HAPTICS_ENABLED_BOOLEAN + " INTEGER NOT NULL DEFAULT 0," +
                        Preferences.BREATHING_CYCLES_INT + " INTEGER DEFAULT NULL)");

                db.execSQL("CREATE TABLE IF NOT EXISTS Module (" +
                        "ID INTEGER PRIMARY KEY," +
                        "Name text)");

                // Fill the Module table with the default values (should not be user-generated)
                for (Module module : Module.values())
                {
                    ContentValues cv = new ContentValues();
                    cv.put("ID", module.id);
                    cv.put("Name", module.name);
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