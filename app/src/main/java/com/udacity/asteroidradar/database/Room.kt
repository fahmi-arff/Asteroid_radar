package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Query("SELECT * FROM databaseentities WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByDate(startDate: String, endDate: String): LiveData<List<DatabaseEntities>>

    @Query("SELECT * FROM databaseentities WHERE closeApproachDate = :today")
    fun getTodayAsteroid(today: String): LiveData<List<DatabaseEntities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseEntities)

    @Query("SELECT * FROM databaseentities ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): LiveData<List<DatabaseEntities>>

    @Query("DELETE FROM databaseentities WHERE closeApproachDate < :today")
    fun deleteYesterdayAsteroids(today: String): Int

}

// room for database builder and access
@Database(entities = [DatabaseEntities::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao

    companion object{
        private lateinit var INSTANCE: AsteroidDatabase

        fun getDatabase(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidDatabase::class.java,
                        "asteroids").build()
                }
            }
            return INSTANCE
        }
    }

}


