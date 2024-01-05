package com.sisensing.common.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntity;
import com.sisensing.common.entity.BloodGlucoseEntity.BloodGlucoseEntityDao;
import com.sisensing.common.entity.Device.DeviceEntity;
import com.sisensing.common.entity.Device.DeviceEntityDao;
import com.sisensing.common.entity.Device.DeviceGlucoseEntity;
import com.sisensing.common.entity.Device.DeviceGlucoseEntityDao;
import com.sisensing.common.entity.LogInfoEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEntity;
import com.sisensing.common.entity.actionRecord.ActionRecordEntityDao;
import com.sisensing.common.entity.clcok.ClockEntity;
import com.sisensing.common.entity.clcok.ClockEntityDao;

/**
 * @ProjectName: CGM_C
 * @Package: com.sisensing.common.database
 * @Author: f.deng
 * @CreateDate: 2021/3/6 15:27
 * @Description:
 */
@Database(entities = {DeviceEntity.class, BloodGlucoseEntity.class, ActionRecordEntity.class, ClockEntity.class, DeviceGlucoseEntity.class, LogInfoEntity.class}, version = 12)
public abstract
class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static void init(Context context) {
        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                AppDatabase.class, "cgm_database")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(MIGRATION_3_4)
                .addMigrations(MIGRATION_4_5)
                .addMigrations(MIGRATION_5_6)
                .addMigrations(MIGRATION_6_7)
                .addMigrations(MIGRATION_7_8)
                .addMigrations(MIGRATION_8_9)
                .addMigrations(MIGRATION_9_10)
                .addMigrations(MIGRATION_10_11)
                .addMigrations(MIGRATION_11_12)
                .build();
    }


    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            throw new NullPointerException("please invoke method init() first in the entry of application");
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE ActionRecordEntity "
                    + " ADD COLUMN uploadService INTEGER NOT NULL DEFAULT 0");
            database.execSQL("ALTER TABLE BloodGlucoseEntity ADD COLUMN electric INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE DeviceEntity "
                    + " ADD COLUMN uploadDeviceStatus INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE DeviceEntity "
                    + " ADD COLUMN algorithmVersion TEXT");

        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE DeviceEntity "
                    + " ADD COLUMN algorithmContext TEXT");
        }
    };

    static final Migration MIGRATION_5_6 = new Migration(5, 6) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE ActionRecordEntity "
                    + " ADD COLUMN oneHoursRepeatClockIn INTEGER NOT NULL DEFAULT 0");

            database.execSQL("ALTER TABLE ActionRecordEntity "
                    + " ADD COLUMN wearClockIn INTEGER NOT NULL DEFAULT 0");

            database.execSQL("ALTER TABLE ActionRecordEntity "
                    + " ADD COLUMN batchActionData TEXT");
        }
    };

    static final Migration MIGRATION_6_7 = new Migration(6, 7) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE DeviceEntity "
                    + " ADD COLUMN algorithmContextIndex  INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_7_8 = new Migration(7, 8) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE ActionRecordEntity "
                    + " ADD COLUMN sgUnit TEXT");
        }
    };

    static final Migration MIGRATION_8_9 = new Migration(8, 9) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
        }
    };

    static final Migration MIGRATION_9_10 = new Migration(9, 10) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something

            database.execSQL("ALTER TABLE DeviceEntity "
                    + " ADD COLUMN characteristic TEXT");

            database.execSQL("CREATE TABLE LogInfoEntity (id INTEGER PRIMARY KEY NOT NULL, timestamp INTEGER NOT NULL, logInfo TEXT)");
        }
    };

    static final Migration MIGRATION_10_11 = new Migration(10, 11) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            //do something
            database.execSQL("ALTER TABLE ActionRecordEntity "
                    + " ADD COLUMN editModel INTEGER NOT NULL DEFAULT 0");
        }
    };

    static final Migration MIGRATION_11_12 = new Migration(11, 12) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 删除旧的唯一性索引
            database.execSQL("DROP INDEX IF EXISTS index_ActionRecordEntity_startTime");

            // 添加新的唯一性索引
            database.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS index_ActionRecordEntity_startTime_dataId ON ActionRecordEntity(startTime, dataId)");
        }
    };

    public abstract DeviceEntityDao getDeviceDao();

    public abstract BloodGlucoseEntityDao getBloodEntityDao();

    public abstract ActionRecordEntityDao getActionRecordEntityDao();

    public abstract ClockEntityDao getClockEntityDao();

    public abstract DeviceGlucoseEntityDao getDeviceGlucoseEntityDao();

    public abstract LogInfoEntityDao getLogInfoEntityDao();
}
