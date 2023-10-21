package com.luisangelnv.socialgame.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.luisangelnv.socialgame.models.Publicaciones;

public class PostProvider {

    CollectionReference mCollection;
    public  PostProvider(){
        mCollection = FirebaseFirestore.getInstance().collection("Posts");
    }
    public Task <Void> save(Publicaciones post){
        return mCollection.document().set(post);
    }
}
