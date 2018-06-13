package textotex.textotex;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class SecureClass extends SQLiteOpenHelper {

    private SQLiteDatabase database;
    private static SharedPreferences mSharedPref;
    private final int mUserID;
    public static final String  DATABASE_NAME = "hashDB";
    public static final int     DATABASE_VERSION = 5;


    public SecureClass(Context context, SharedPreferences sharedPref) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mSharedPref = sharedPref;
        this.mUserID = this.mSharedPref.getInt(context.getString(R.string.user_id_key), -1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String rq1 = "CREATE TABLE IF NOT EXISTS PublicKeys(idPubKey INTEGER NOT NULL AUTOINCREMENT, idUser INTEGER NOT NULL, pub_key TEXT);";
        final String rq2 = "CREATE TABLE IF NOT EXISTS AESKeys (idAESKey INTEGER NOT NULL AUTOINCREMENT, idConversation INTEGER NOT NULL, private_key TEXT, aes_key TEXT NOT NULL);";

        database = SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null);

        database.execSQL(rq1);
        database.execSQL(rq2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String rq1 = "DROP TABLE IF EXISTS PublicKeys;";
        String rq2 = "DROP TABLE IF EXISTS AESKeys;";

        database.execSQL(rq1);
        database.execSQL(rq2);

        onCreate(db);
    }

    public void newRSAKey(int conversationID) {

        try {
            final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            KeyPair kp = generator.generateKeyPair();

            this.insertKeys(conversationID, kp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertKeys(int conversationID, KeyPair kp)
    {
        String rq_insert_public = "INSERT INTO PublicKeys (idUser, pub_key) VALUES (" + Integer.toString(this.mUserID) + ", " + kp.getPublic() + ")";
        String rq_insert_private = "INSERT INTO AESKeys (idConversation, priv_key) VALUES (" + Integer.toString(conversationID) + kp.getPrivate() + ")";

        database.execSQL(rq_insert_public);
        database.execSQL(rq_insert_private);

    }
}


//[P#1:AndroidOpenSSL][S#2:Cipher][A#1:AES/CBC/NoPadding]
//[P#1:AndroidOpenSSL][S#2:Cipher][A#2:AES/CBC/PKCS5Padding]
//[P#1:AndroidOpenSSL][S#2:Cipher][A#3:AES/CBC/PKCS7Padding]

//[P#1:AndroidOpenSSL][S#5:KeyPairGenerator][A#6:RSA]

//[P#3:BC][S#11:KeyPairGenerator][A#1:1.2.840.10040.4.1]
//[P#3:BC][S#11:KeyPairGenerator][A#2:1.2.840.10045.2.1]
//[P#3:BC][S#11:KeyPairGenerator][A#3:1.2.840.113549.1.1.1]
//[P#3:BC][S#11:KeyPairGenerator][A#4:1.2.840.113549.1.1.7]
//[P#3:BC][S#11:KeyPairGenerator][A#5:1.2.840.113549.1.3.1]
//[P#3:BC][S#11:KeyPairGenerator][A#6:1.3.133.16.840.63.0.2]
//[P#3:BC][S#11:KeyPairGenerator][A#7:1.3.14.3.2.27]
//[P#3:BC][S#11:KeyPairGenerator][A#8:2.5.8.1.1]
//[P#3:BC][S#11:KeyPairGenerator][A#9:DH]
//[P#3:BC][S#11:KeyPairGenerator][A#10:DIFFIEHELLMAN]
//[P#3:BC][S#11:KeyPairGenerator][A#11:DSA]
//[P#3:BC][S#11:KeyPairGenerator][A#12:EC]
//[P#3:BC][S#11:KeyPairGenerator][A#13:RSA]

