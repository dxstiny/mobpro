package ch.hslu.diashorta.persistence.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var title: String? = null,
    var content: String? = null,
    var modified: Date? = null
)