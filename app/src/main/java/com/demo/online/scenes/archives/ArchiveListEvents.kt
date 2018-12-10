package com.demo.online.scenes.archives

import com.demo.api.Archive


/**
 * Created by Midas on 03.03.2018.
 */
interface ArchiveListEvents {
    fun selectArchive(archive_title:String)
    fun removeArchive(archive: Archive)
}