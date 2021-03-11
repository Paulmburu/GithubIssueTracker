package github.Paulmburu.githubissuetracker.utils


import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
fun String.convertDate(): String {
    return LocalDateTime
        .parse(this, DateTimeFormatter.ofPattern(Constants.REMOTE_DATE_FORMAT))
        .toLocalDate()
        .format(
            DateTimeFormatter
                .ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.ENGLISH)
        )
}

fun String.convertDateUsingLegacyClasses() : String {
    val formatter = SimpleDateFormat(Constants.REMOTE_DATE_FORMAT)

    return try {
        formatter.parse(this).toString()
    } catch (e: ParseException) {
        e.printStackTrace().toString()
    }
}

fun String.validateIfEmpty(): String = if(this.isEmpty()) "..." else this