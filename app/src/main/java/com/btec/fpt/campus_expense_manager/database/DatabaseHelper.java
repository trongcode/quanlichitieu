package com.btec.fpt.campus_expense_manager.database;

import static android.app.DownloadManager.COLUMN_ID;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.entities.User;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ExpenseDB";
    private static final int DATABASE_VERSION = 4;

    // Transactions table
    private static final String TABLE_TRANSACTION = "transactions";
    private static final String COLUMN_TRANSACTION_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";


    // User table
    private static final String TABLE_USER = "USER";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_LAST_NAME = "lastName";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    // Category table
    private static final String TABLE_CATEGORY = "CATEGORY";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";

    public  DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create transactions table
        String CREATE_TRANSACTION_TABLE = "CREATE TABLE " + TABLE_TRANSACTION + "("
                + COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TYPE + " INTEGER, "
                + COLUMN_EMAIL + " TEXT ,"
                + COLUMN_CATEGORY + " TEXT"  + ")";

//
        db.execSQL(CREATE_TRANSACTION_TABLE);


        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " TEXT, "
                + COLUMN_LAST_NAME + " TEXT, "
                + COLUMN_EMAIL + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);


        // Create category table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + "("
                + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_CATEGORY_NAME + " TEXT UNIQUE NOT NULL,"
                + COLUMN_EMAIL + " TEXT "+

                ")";
        db.execSQL(CREATE_CATEGORY_TABLE);

        insertDefaultCategories(db, null);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // Nếu phiên bản cũ nhỏ hơn 2, thêm cột category_id
            db.execSQL("ALTER TABLE " + TABLE_TRANSACTION + " ADD COLUMN " + COLUMN_CATEGORY_ID + " INTEGER");
            db.execSQL("PRAGMA foreign_keys=on;"); // Kích hoạt hỗ trợ khóa ngoại
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }


    // Insert a new transaction record
    public boolean insertTransaction(double amount, String description, String date,
                                     int type, String email, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_CATEGORY, category);

        try {
            long result = db.insert(TABLE_TRANSACTION, null, values);
            return result != -1; // Trả về true nếu thêm thành công
        } catch (Exception e) {
            Log.e("Database Error", "Error inserting transaction", e);
            return false; // Trả về false nếu có lỗi
        } finally {
            db.close();
        }
    }



    public boolean updateTransaction(int id, double amount, String description, String date, int type, String email, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TYPE, type);
        values.put(COLUMN_EMAIL, email);

        values.put(COLUMN_CATEGORY, category);

        int rowsAffected = db.update(TABLE_TRANSACTION, values, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteTransaction(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_TRANSACTION, COLUMN_TRANSACTION_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted > 0;
    }

    // Insert a new user record
    public boolean insertUser(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result != -1;
    }
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra email và mật khẩu hiện tại
        Cursor cursor = db.query(
                TABLE_USER,
                new String[]{COLUMN_PASSWORD},
                COLUMN_EMAIL + " = ?",
                new String[]{email},
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // Lấy mật khẩu lưu trữ trong cơ sở dữ liệu
            String storedPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));

            // Kiểm tra mật khẩu hiện tại
            if (storedPassword.equals(currentPassword)) {
                // Nếu đúng, cập nhật mật khẩu mới
                ContentValues values = new ContentValues();
                values.put(COLUMN_PASSWORD, newPassword);

                int rowsAffected = db.update(
                        TABLE_USER,
                        values,
                        COLUMN_EMAIL + " = ?",
                        new String[]{email}
                );

                cursor.close();
                db.close();

                // Trả về true nếu cập nhật thành công
                return rowsAffected > 0;
            }
        }

        if (cursor != null) {
            cursor.close();
        }
        db.close();

        // Trả về false nếu mật khẩu hiện tại không đúng hoặc không tìm thấy email
        return false;
    }


    public boolean updateUser(int userId, String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        int rowsAffected = db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USER, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsDeleted > 0;
    }

    // Insert a new category record
    public boolean insertCategory(String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_CATEGORY, null, values);
        db.close();
        return result != -1;
    }


    public boolean updateCategory(int categoryId, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        values.put(COLUMN_EMAIL, email);

        int rowsAffected = db.update(TABLE_CATEGORY, values, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_ID + " = ?", new String[]{String.valueOf(categoryId)});
        db.close();
        return rowsDeleted > 0;
    }


    // Retrieve all categories as a List<Category>
    public ArrayList<String> getCategories(String email) {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_EMAIL + " = ? OR " + COLUMN_EMAIL + " IS NULL",
                new String[]{email}
        );

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }


    public List<String> getAllCategoryNamesByEmail(String email) {
        List<String> categoryNames = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Prepare the query and parameters based on whether email is null
        String query;
        String[] queryParams;

        if (email == null) {
            query = "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_EMAIL + " IS NULL";
            queryParams = null;
        } else {
            query = "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY +
                    " WHERE " + COLUMN_EMAIL + " = ? OR " + COLUMN_EMAIL + " IS NULL";
            queryParams = new String[]{email};
        }

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, queryParams);

            // Extract category names from the result
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME));
                    categoryNames.add(name);
                } while (cursor.moveToNext());
            }
        } finally {
            // Ensure cursor and database are closed to avoid leaks
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return categoryNames;
    }

    public int getCategoryIdByName(String categoryName, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORY, new String[]{COLUMN_CATEGORY_ID}, COLUMN_CATEGORY_NAME + " = ? AND " + COLUMN_EMAIL + " = ?",
                new String[]{categoryName, email}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                @SuppressLint("Range") int categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                cursor.close();
                return categoryId;
            }
            cursor.close();
        }
        return -1; // Not found
    }





//    public List<Transaction> getTransactionsByEmail(String email) {
//        List<Transaction> transactionList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Query to join transactions with categories
//        String query = "SELECT t." + COLUMN_TRANSACTION_ID + ", t." + COLUMN_AMOUNT + ", t." + COLUMN_DESCRIPTION +
//                ", t." + COLUMN_DATE + ", t." + COLUMN_TYPE + ", t." + COLUMN_EMAIL +
//                ", c." + COLUMN_CATEGORY_NAME +
//                " FROM " + TABLE_TRANSACTION + " t " +
//                "LEFT JOIN " + TABLE_CATEGORY + " c ON t." + COLUMN_CATEGORY_ID + " = c." + COLUMN_CATEGORY_ID +
//                " WHERE t." + COLUMN_EMAIL + " = ?";
//
//        Cursor cursor = db.rawQuery(query, new String[]{email});
//
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
//                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
//                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
//                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
//                @SuppressLint("Range") String emailFromDb = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
//                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
//
//                // Create a transaction object
//                Transaction transaction = new Transaction(id, amount, description, date, type, emailFromDb, categoryName);
//                transactionList.add(transaction);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//        return transactionList;
//    }




    // Retrieve all transactions

    public boolean emailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn SQL kiểm tra sự tồn tại của email
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        // Nếu số bản ghi trả về > 0, có nghĩa là email tồn tại
        boolean exists = cursor.getCount() > 0;

        // Đóng cursor và database
        cursor.close();
        db.close();

        return exists;
    }

    // Retrieve all transactions for a user by email
    public List<Transaction> getAllTransactionsByEmail(String email) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String email2 = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));

                Transaction transaction = new Transaction(id, amount, description, date,type, email2, category );
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return transactionList;
    }


    /////////
    public List<Transaction> getAllTransactionsByCategory(String email, String category) {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Câu lệnh SQL để lọc theo email và type
        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_CATEGORY + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, String.valueOf(category)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int typeValue = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String emailValue = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)); // Sửa đúng tên cột

                // Tạo đối tượng Transaction
                Transaction transaction = new Transaction(id, amount, description, date, typeValue, emailValue, categoryName);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionList;
    }

    public List<Transaction> getAllTransactionsByType(String email,int type)
    {
        List<Transaction> transactionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Câu lệnh SQL để lọc theo email và type
        String query = "SELECT * FROM " + TABLE_TRANSACTION + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_TYPE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, String.valueOf(type)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") int typeValue = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
                @SuppressLint("Range") String emailValue = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)); // Sửa đúng tên cột

                // Tạo đối tượng Transaction
                Transaction transaction = new Transaction(id, amount, description, date, typeValue, emailValue, category);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return transactionList;
    }

    // Lấy tất cả các giao dịch thu nhập cho người dùng theo email
//    public List<Transaction> getAllIncomesByEmail(String email) {
//        List<Transaction> transactionList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Query to fetch income transactions with category names
//        String query = "SELECT t." + COLUMN_TRANSACTION_ID + ", t." + COLUMN_AMOUNT + ", t." + COLUMN_DESCRIPTION +
//                ", t." + COLUMN_DATE + ", t." + COLUMN_TYPE + ", t." + COLUMN_EMAIL +
//                ", c." + COLUMN_CATEGORY_NAME +
//                " FROM " + TABLE_TRANSACTION + " t " +
//                "LEFT JOIN " + TABLE_CATEGORY + " c ON t." + COLUMN_CATEGORY_NAME + " = c." + COLUMN_CATEGORY_NAME +
//                " WHERE t." + COLUMN_EMAIL + " = ? AND t." + COLUMN_TYPE + " = 1";
//
//        Cursor cursor = db.rawQuery(query, new String[]{email});
//
//        if (cursor.moveToFirst()) {
//            do {
//                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_TRANSACTION_ID));
//                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT));
//                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
//                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
//                @SuppressLint("Range") int type = cursor.getInt(cursor.getColumnIndex(COLUMN_TYPE));
//                @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
//                @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME));
//
//                // Create transaction object with category name
//                Transaction transaction = new Transaction(id, amount, description, date, type, userEmail, categoryName);
//                transactionList.add(transaction);
//            } while (cursor.moveToNext());
//        }
//
//        cursor.close();
//        db.close();
//        return transactionList;
//    }



    @SuppressLint("Range")
    public int getCategoryIdByNameD(String categoryName, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_CATEGORY_ID +
                        " FROM " + TABLE_CATEGORY +
                        " WHERE " + COLUMN_CATEGORY_NAME + " = ? AND " + COLUMN_EMAIL + " = ?",
                new String[]{categoryName, email});

        int categoryId = -1; // Default if no category found
        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndex(COLUMN_CATEGORY_ID));
        }
        cursor.close();
        db.close();
        return categoryId;
    }

    // Function to get a user by email
    public User getUserByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        if (cursor.moveToFirst()) {
            // Extract user details from the cursor
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
            @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
            @SuppressLint("Range") String userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

            // Create a User object with the retrieved data
            user = new User(id, firstName, lastName, userEmail, password);
        }

        cursor.close();
        db.close();
        return user;
    }

    // Retrieve all users
    public List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID));
                @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
                @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

                User user = new User(id, firstName, lastName, email, password);
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }


    public BalanceInfor getBalanceFromEmail(String email){

        User userFound = getUserByEmail(email);
        if(userFound!=null){

            String firstName = userFound.getFirstName();
            String lastName = userFound.getLastName();


            List<Transaction> allTransaction = getAllTransactionsByEmail(email);

            double expense = 0;
            double income = 0;
            double balance = 0;
            for(Transaction transaction: allTransaction){

                if(transaction.getType()==0){
                    expense += transaction.getAmount();
                }else if(transaction.getType()==1){
                    income += transaction.getAmount();
                }
            }

            balance = income - expense;

            BalanceInfor balanceInfor = new BalanceInfor();
            balanceInfor.setBalance(balance);
            balanceInfor.setFirstName(firstName);
            balanceInfor.setLastName(lastName);
            balanceInfor.setEmail(email);

            return  balanceInfor;

        }


        return  null;
    }


    // Method to insert default categories into the database
    private void insertDefaultCategories(SQLiteDatabase db, String email) {
        String[] defaultCategories = {"Food", "Transport", "Entertainment", "Utilities", "Health"};

        for (String categoryName : defaultCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CATEGORY_NAME, categoryName);
            values.put(COLUMN_EMAIL, email); // Set email or leave it null for default
            db.insert(TABLE_CATEGORY, null, values);
        }
    }

    // Helper method to hash the password
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.encodeToString(hash, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Sign Up function
    public boolean signUp(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return false; // User already exists
        }

        String hashedPassword = hashPassword(password); // Hash the password
        if (hashedPassword == null) {
            cursor.close();
            db.close();
            return false; // Hashing failed
        }

        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, hashedPassword);

        long result = db.insert(TABLE_USER, null, values);
        cursor.close();
        db.close();
        return result != -1;
    }

    // Sign In function
    public boolean signIn(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String hashedPassword = hashPassword(password); // Hash the password for comparison
        if (hashedPassword == null) {
            db.close();
            return false; // Hashing failed
        }

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, hashedPassword});

        boolean isAuthenticated = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return isAuthenticated;
    }

}