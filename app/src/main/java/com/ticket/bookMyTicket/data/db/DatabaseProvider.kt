import android.content.Context
import androidx.room.Room
import com.ticket.bookMyTicket.data.db.BookMyTicketDatabase

object DatabaseProvider {

    private var INSTANCE: BookMyTicketDatabase? = null

    fun getDatabase(context: Context): BookMyTicketDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                BookMyTicketDatabase::class.java,
                "ticket_db"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}