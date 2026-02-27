import android.content.Context
import com.example.lab1.R
import java.util.Properties

object Config {
    private val properties = Properties()

    fun loadConfig(context: Context) {
        try {
            val inputStream = context.resources.openRawResource(R.raw.config)
            properties.load(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getDbUrl(): String = properties.getProperty("db.url")
    fun getApi(): String = properties.getProperty("api.code")
}