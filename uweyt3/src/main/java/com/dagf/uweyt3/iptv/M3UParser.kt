package com.dagf.uweyt3.iptv

import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class M3UParser {

    private val EXT_M3U = "#EXTM3U"
    private val EXT_PLAYLIST_NAME = "#PLAYLIST"
    private val EXT_INF = "#EXTINF"
    private val EXT_LOGO = "tvg-logo"
    private val EXT_URL = "http://"

    fun parseFile(inputStream: InputStream): ArrayList<M3UItem> {
        val playListItems = ArrayList<M3UItem>()
        val stream = convertStreamToString(inputStream)
        val lineArray = stream.split(EXT_INF)

        for (i in lineArray.indices) {
            try {
                val currentLine = lineArray[i]
                if (currentLine.contains(EXT_M3U)) {
                    continue
                } else {
                    val m3UItem = M3UItem()
                    val dataArray = currentLine.split(",")
                    if (dataArray[0].contains(EXT_LOGO)) {
                        val data = dataArray[0].split(" ")
                        for (position in data.indices) {
                            if (data[position].contains(EXT_LOGO)) {
                                m3UItem.tvIcon = data[position].replace("\"", "").split("=").get(1)
                                break
                            }
                        }
                    }

                    val data = dataArray[1].split("\n")
                    m3UItem.tvName = data[0]
                    m3UItem.tvURL = data[1]

                    playListItems.add(m3UItem)
                }
            }
            catch (e: IndexOutOfBoundsException) { continue }
        }

        inputStream.close()
        return playListItems
    }

    private fun convertStreamToString(`is`: InputStream): String {
        try {
            return Scanner(`is`).useDelimiter("\\A").next()
        } catch (e: NoSuchElementException) {
            return ""
        }
    }
}