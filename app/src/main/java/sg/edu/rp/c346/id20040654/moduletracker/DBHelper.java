package sg.edu.rp.c346.id20040654.moduletracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "module_tracker.db";
    private static final int DATABASE_VERSION = 1;
    // first table
    private static final String TABLE_MODULE = "module";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_CODE = "code";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_BREAKDOWN = "breakdown";
    // second table
    private static final String TABLE_CAG = "cag";
    // for id, just take same variable as first table
    private static final String COLUMN_AKS = "aks";
    private static final String COLUMN_SDL = "sdl";
    private static final String COLUMN_COL = "col";
    private static final String COLUMN_MODID = "module_id";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating sql table in database
        String createModuleTableSql = "CREATE TABLE " + TABLE_MODULE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CODE + " TEXT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_BREAKDOWN + " INTEGER ) ";
        db.execSQL(createModuleTableSql);
        Log.i("info", "created first table");

        String createCagTableSql = "CREATE TABLE " + TABLE_CAG + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_AKS + " REAL,"
                + COLUMN_SDL + " REAL,"
                + COLUMN_COL + " REAL,"
                + COLUMN_MODID + " INTEGER, FOREIGN KEY(" + COLUMN_MODID + ") REFERENCES " + TABLE_MODULE + "(" + COLUMN_ID + ") )";
        db.execSQL(createCagTableSql);
        Log.i("info", "created second table");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAG);
        onCreate(db);
    }

    // Insert
    public long insertModule(Module data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // values.put(COLUMN_ID, data.getId());
        values.put(COLUMN_CODE, data.getCode());
        values.put(COLUMN_NAME, data.getName());
        values.put(COLUMN_BREAKDOWN, data.getBreakdown());
        long result = db.insert(TABLE_MODULE, null, values); // result returns the id of the new row
        db.close();
        Log.d("SQL Insert Module","ID: "+ result); //id returned, shouldn’t be -1
//        int resultId = (int) result;
//        data.setId(resultId);
        return result;
    }

    public long insertCag(CAG data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AKS, data.getAks());
        values.put(COLUMN_SDL, data.getSdl());
        values.put(COLUMN_COL, data.getCol());
        values.put(COLUMN_MODID, data.getModuleId());
        long result = db.insert(TABLE_CAG, null, values); // result returns the id of the new row
        db.close();
        Log.d("SQL Insert CAG","ID: "+ result); //id returned, shouldn’t be -1
//        int resultId = (int) result;
//        data.setId(resultId);
        return result;
    }

    public ArrayList<Module> getAllModules() {
        ArrayList<Module> modules = new ArrayList<Module>();

        String selectQuery = "SELECT * FROM " + TABLE_MODULE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String code = cursor.getString(1);
                String name = cursor.getString(2);
                int breakdown = cursor.getInt(3);
                Module module = new Module(id, breakdown, code, name);
                modules.add(module);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return modules;
    }

    public ArrayList<CAG> getAllCAG(Module module) {
        ArrayList<CAG> cags = new ArrayList<CAG>();

        String selectQuery = "SELECT * FROM " + TABLE_CAG
                + " INNER JOIN " + TABLE_MODULE
                + " ON " + TABLE_MODULE + "." + COLUMN_ID + " = " + TABLE_CAG + "." + COLUMN_MODID
                + " WHERE " + TABLE_MODULE + "." + COLUMN_CODE + " = ?";
        String[] args = {module.getCode()};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, args);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                float aks = cursor.getFloat(1);
                float sdl = cursor.getFloat(2);
                float col = cursor.getFloat(3);
                int modId = cursor.getInt(4);
                CAG cag = new CAG(module.getModuleId(), module.getBreakdown(), module.getCode(), module.getName(), id, aks, sdl, col);
                cags.add(cag);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cags;
    }

    public int updateModule(Module data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_ID, data.getId());
        values.put(COLUMN_CODE, data.getCode());
        values.put(COLUMN_NAME, data.getName());
        values.put(COLUMN_BREAKDOWN, data.getBreakdown());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getModuleId())};
        int result = db.update(TABLE_MODULE, values, condition, args); // result returns the number of rows updated
        if (result < 1) {
            Log.d("DBHelper", "Update failed");
        }
        db.close();
        return result;
    }

    public int updateCAG(CAG data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(COLUMN_ID, data.getId());
        values.put(COLUMN_AKS, data.getAks());
        values.put(COLUMN_SDL, data.getSdl());
        values.put(COLUMN_COL, data.getCol());
        values.put(COLUMN_MODID, data.getModuleId());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getModuleId())};
        int result = db.update(TABLE_CAG, values, condition, args); // result returns the number of rows updated
        if (result < 1) {
            Log.d("DBHelper", "Update failed");
        }
        db.close();
        return result;
    }

    public int[] deleteModule(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        // delete child
        String childCondition = COLUMN_MODID + "= ?";
        String[] args = {String.valueOf(id)};
        int childResult = db.delete(TABLE_CAG, childCondition, args);
        if (childResult < 1) {
            Log.d("DBHelper", "Delete child failed");
        }
        // delete parent
        String parentCondition = COLUMN_ID + "= ?";
        int parentResult = db.delete(TABLE_MODULE, parentCondition, args);
        if (parentResult < 1) {
            Log.d("DBHelper", "Delete parent failed");
        }
        int[] result = {childResult, parentResult};
        db.close();
        return result;
    }

    public int deleteCAG(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String childCondition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_CAG, childCondition, args);
        if (result < 1) {
            Log.d("DBHelper", "Delete failed");
        }
        db.close();
        return result;
    }

}
