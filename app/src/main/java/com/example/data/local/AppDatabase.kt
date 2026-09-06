package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.PlaceEntity
import com.example.data.util.DigipinUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PlaceEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "hidden_indian_database"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialPlaces(database.placeDao())
                    }
                }
            }
        }

        suspend fun populateInitialPlaces(dao: PlaceDao) {
            if (dao.getCount() > 0) return

            val seedPlaces = listOf(
                PlaceEntity(
                    id = "place_1",
                    name = "Tirthan Valley",
                    description = "A tranquil paradise in the lap of the Himalayas along the pristine Tirthan River.",
                    longDescription = "Tirthan Valley is a hidden gem in Himachal Pradesh, known for its crystal clear river, lush green forests and peaceful villages. It's perfect for nature lovers, trekkers and anyone looking to escape the crowd into Great Himalayan National Park gateway.",
                    category = "Waterfalls",
                    state = "Himachal Pradesh",
                    city = "Kullu",
                    latitude = 31.6366,
                    longitude = 77.3456,
                    digipin = DigipinUtil.encode(31.6366, 77.3456),
                    mapplseLoc = "86H6+RW",
                    hiddenScore = 92,
                    beauty = 92,
                    crowd = 85,
                    accessibility = 61,
                    adventure = 78,
                    uniqueness = 90,
                    bestTimeToVisit = "March – June, September – November",
                    safetyInfo = "Moderate. Carry trekking gear, valid id for GHNP entry, check rain forecasts.",
                    rating = 4.9f,
                    reviewsCount = 42,
                    savesCount = 128,
                    isSaved = true,
                    imageDrawableName = "tirthan_valley_1788715016404",
                    addedBy = "Aakash Singh"
                ),
                PlaceEntity(
                    id = "place_2",
                    name = "Kedarkantha Trek",
                    description = "A stunning winter summit trek nestled amidst Garhwal pine forests and snow ridges.",
                    longDescription = "Kedarkantha stands at 12,500 ft in the Govind Wildlife Sanctuary. Renowned for its 360-degree panorama of mountain peaks including Swargarohini, Bandarpoonch, and Black Peak, it offers magical snow campsites in untouched wilderness.",
                    category = "Mountains",
                    state = "Uttarakhand",
                    city = "Sankri",
                    latitude = 31.0232,
                    longitude = 78.1725,
                    digipin = DigipinUtil.encode(31.0232, 78.1725),
                    mapplseLoc = "42KK+99",
                    hiddenScore = 88,
                    beauty = 95,
                    crowd = 72,
                    accessibility = 55,
                    adventure = 92,
                    uniqueness = 86,
                    bestTimeToVisit = "December – April (Snow), May – June",
                    safetyInfo = "High Altitude. Acclimatization required, use microspikes on hard ice.",
                    rating = 4.8f,
                    reviewsCount = 56,
                    savesCount = 210,
                    isSaved = true,
                    imageDrawableName = "kedarkantha_trek_1788715045426",
                    addedBy = "Himalayan_Explorer"
                ),
                PlaceEntity(
                    id = "place_3",
                    name = "Chopta Valley",
                    description = "Known as the 'Mini Switzerland of India', offering alpine meadows and Tungnath peak trail.",
                    longDescription = "Chopta is an unspoiled natural meadow located in Kedarnath Wildlife Sanctuary. It serves as the base for hiking to Tungnath, the highest Shiva temple in the world, and further up to Chandrashila summit at 13,000 ft.",
                    category = "Mountains",
                    state = "Uttarakhand",
                    city = "Rudraprayag",
                    latitude = 30.4853,
                    longitude = 79.1764,
                    digipin = DigipinUtil.encode(30.4853, 79.1764),
                    mapplseLoc = "77RP+CV",
                    hiddenScore = 84,
                    beauty = 91,
                    crowd = 78,
                    accessibility = 70,
                    adventure = 80,
                    uniqueness = 88,
                    bestTimeToVisit = "April – June, October – November",
                    safetyInfo = "Moderate. High altitude steep steps near Tungnath temple; warm layers essential.",
                    rating = 4.7f,
                    reviewsCount = 38,
                    savesCount = 145,
                    isSaved = false,
                    imageDrawableName = "chopta_valley_1788715064959",
                    addedBy = "Aakash Singh"
                ),
                PlaceEntity(
                    id = "place_4",
                    name = "Ziro Valley",
                    description = "Verdant pine-clad hills and rice paddies home to the distinctive Apatani tribe.",
                    longDescription = "Enclosed by dramatic pine hills in Arunachal Pradesh, Ziro is a UNESCO World Heritage nominee. Its sustainable paddy-cum-pisciculture farming, bamboo groves, and ancient traditions make it an extraordinary cultural haven.",
                    category = "Villages",
                    state = "Arunachal Pradesh",
                    city = "Ziro",
                    latitude = 27.5950,
                    longitude = 93.8340,
                    digipin = DigipinUtil.encode(27.5950, 93.8340),
                    mapplseLoc = "38ZR+7F",
                    hiddenScore = 76,
                    beauty = 88,
                    crowd = 90,
                    accessibility = 45,
                    adventure = 72,
                    uniqueness = 94,
                    bestTimeToVisit = "March – October",
                    safetyInfo = "ILP (Inner Line Permit) mandatory for domestic and foreign travelers.",
                    rating = 4.9f,
                    reviewsCount = 29,
                    savesCount = 98,
                    isSaved = false,
                    imageDrawableName = "",
                    addedBy = "NorthEastVibes"
                ),
                PlaceEntity(
                    id = "place_5",
                    name = "Yana Caves",
                    description = "Gigantic black crystalline karst limestone monoliths rising from dense Sahyadri rainforests.",
                    longDescription = "Located in the lush Western Ghats of Uttara Kannada, Bhairaveshwara Shikhara and Mohini Shikhara are two towering solid black rock formations rising 90 and 120 meters amidst thick canopies with a subterranean stream flowing below.",
                    category = "Historical",
                    state = "Karnataka",
                    city = "Kumta",
                    latitude = 14.5886,
                    longitude = 74.5614,
                    digipin = DigipinUtil.encode(14.5886, 74.5614),
                    mapplseLoc = "19YN+82",
                    hiddenScore = 79,
                    beauty = 86,
                    crowd = 82,
                    accessibility = 65,
                    adventure = 75,
                    uniqueness = 92,
                    bestTimeToVisit = "October – February",
                    safetyInfo = "Dense rainforest trail. Beware of leeches during monsoon season.",
                    rating = 4.6f,
                    reviewsCount = 24,
                    savesCount = 112,
                    isSaved = true,
                    imageDrawableName = "",
                    addedBy = "Aakash Singh"
                ),
                PlaceEntity(
                    id = "place_6",
                    name = "Malana Village",
                    description = "Ancient secluded settlement perched high on Parvati Valley claiming Greek lineage.",
                    longDescription = "An isolated village in Malana Nala side valley, renowned for its independent social structure, Jamlu Rishi council, unique Kanashi language, and strict customs where outsiders must respect sacred boundary markers.",
                    category = "Villages",
                    state = "Himachal Pradesh",
                    city = "Kasol",
                    latitude = 32.0628,
                    longitude = 77.2625,
                    digipin = DigipinUtil.encode(32.0628, 77.2625),
                    mapplseLoc = "99ML+31",
                    hiddenScore = 76,
                    beauty = 89,
                    crowd = 70,
                    accessibility = 50,
                    adventure = 85,
                    uniqueness = 96,
                    bestTimeToVisit = "May – October",
                    safetyInfo = "Respect village laws; do not touch walls, shrines, or residents without consent.",
                    rating = 4.5f,
                    reviewsCount = 31,
                    savesCount = 87,
                    isSaved = false,
                    imageDrawableName = "",
                    addedBy = "KasolTrekker"
                ),
                PlaceEntity(
                    id = "place_7",
                    name = "India Gate",
                    description = "Monumental war memorial archway on Kartavya Path in the heart of the national capital.",
                    longDescription = "Located in New Delhi, India Gate honors the soldiers of the Indian Army who fell during the First World War. Set against sprawling green lawns, it is a national symbol of valor.",
                    category = "Historical",
                    state = "New Delhi",
                    city = "New Delhi",
                    latitude = 28.6129,
                    longitude = 77.2295,
                    digipin = "FFF-3MF-34F8",
                    mapplseLoc = "86H6+RW",
                    hiddenScore = 32,
                    beauty = 85,
                    crowd = 15,
                    accessibility = 98,
                    adventure = 20,
                    uniqueness = 75,
                    bestTimeToVisit = "October – March (Evening lights)",
                    safetyInfo = "High security zone. Accessible via Delhi Metro Central Secretariat.",
                    rating = 4.7f,
                    reviewsCount = 1200,
                    savesCount = 540,
                    isSaved = false,
                    imageDrawableName = "",
                    addedBy = "Aakash Singh"
                )
            )

            dao.insertPlaces(seedPlaces)
        }
    }
}
