package com.eachilin.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception

class EventLike(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object{
        private val DATABASE_VER = 1
        private val DATABASE_NAME = "EventLike.db"

        private val TABLE_NAME = "eventlike"
        private val ID ="id"
        private val EVENTID = "eventID"
        private val FAVORITE = "favorite"
    }

    override fun onCreate(db: SQLiteDatabase){
        val CREATE_TABLE_QUERY : String = ("CREATE TABLE " + TABLE_NAME + " ( "
                + EVENTID + " INTEGER PRIMARY KEY, " +
                FAVORITE + " Integer  )"
                )
        db.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS  $TABLE_NAME")
        onCreate(db)
    }

    fun addNewLike(event: EventLikeModal){
        val db = this.readableDatabase
        val contentValue = ContentValues()

        contentValue.put(EVENTID, event.eventID)
        contentValue.put(FAVORITE, event.favorite)
        db.insert(TABLE_NAME, null, contentValue)
        db.close()
    }

    @SuppressLint("Range", "Recycle")
    fun getAllEventLikes() : ArrayList<EventLikeModal>{
        val eventLikes = ArrayList<EventLikeModal>()
        val selectQuery = "Select * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch(e:Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var eventID :Int
        var favorite :Int

        if(cursor != null && cursor.count > 0){
            if(cursor.moveToFirst()) {
                do{
                    eventID = cursor.getInt(cursor.getColumnIndex("eventID"))
                    favorite = cursor.getInt(cursor.getColumnIndex("favorite"))

                    val event = EventLikeModal( eventID = eventID, favorite = favorite)
                    eventLikes.add(event)
                }while (cursor.moveToNext())
            }
            }
        cursor.close()
        db.close()

        return eventLikes
    }

    fun checkLikeExist(id: String) : Boolean {
        val query = "Select $EVENTID from $TABLE_NAME Where $EVENTID = $id"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if(cursor.count <= 0){
            cursor.close()
            db.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }


    fun deleteLike(id: Int ):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TABLE_NAME, "eventID=$id", null)
        db.close()
        return success
    }

}