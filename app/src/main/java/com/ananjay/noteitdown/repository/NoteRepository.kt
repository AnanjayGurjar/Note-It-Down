package com.ananjay.noteitdown.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ananjay.noteitdown.api.NotesApi
import com.ananjay.noteitdown.models.NoteRequest
import com.ananjay.noteitdown.models.NoteResponse
import com.ananjay.noteitdown.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor(private val notesApi: NotesApi) {

    private val _notesLiveData = MutableLiveData<NetworkResult<List<NoteResponse>>>()
    val notesLiveData : LiveData<NetworkResult<List<NoteResponse>>>
    get() = _notesLiveData

    //to collect the status we get while performing all the crud operations on the note
    private val _statusLiveData = MutableLiveData<NetworkResult<String>>()
    val statusLiveData : LiveData<NetworkResult<String>>
        get() = _statusLiveData

    suspend fun getNotes(){
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.getNotes()
        if(response.isSuccessful && response.body() != null){
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        }else if(response.errorBody() != null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }else{
            _notesLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun createNote(noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.createNote(noteRequest)
        handleResposne(response, "Note Created Successfully")
    }

    suspend fun deleteNote(noteId: String){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.deleteNote(noteId)
        handleResposne(response, "Note Deleted Successfully")
    }

    suspend fun updateNote(noteId: String, noteRequest: NoteRequest){
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = notesApi.updateNote(noteId, noteRequest)
        handleResposne(response, "Note Updated Successfully")
    }

    private fun handleResposne(response: Response<NoteResponse>, message: String){
        if(response.isSuccessful && response.body() != null){
            _statusLiveData.postValue(NetworkResult.Success(message))
        }else{
            _statusLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }
}