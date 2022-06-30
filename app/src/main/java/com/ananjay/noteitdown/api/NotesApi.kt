package com.ananjay.noteitdown.api

import com.ananjay.noteitdown.models.NoteRequest
import com.ananjay.noteitdown.models.NoteResponse
import retrofit2.Response
import retrofit2.http.*

interface NotesApi {
    @GET("/note")
    suspend fun getNotes() : Response<List<NoteResponse>>

    @DELETE("/note/{noteId}")
    suspend fun deleteNote(@Path("noteId") noteId: String) : Response<NoteResponse>

    @PUT("/note/{noteId}")
    suspend fun updateNote(@Path("noteId") noteId: String,@Body noteRequest: NoteRequest) : Response<NoteResponse>

    @POST("/note")
    suspend fun createNote(@Body noteRequest: NoteRequest) : Response<NoteResponse>
}