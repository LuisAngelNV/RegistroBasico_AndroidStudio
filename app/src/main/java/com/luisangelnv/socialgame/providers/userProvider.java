package com.luisangelnv.socialgame.providers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.luisangelnv.socialgame.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class userProvider {
    private CollectionReference mColection;
    public userProvider(){
        mColection = FirebaseFirestore.getInstance().collection("Users");
    }
    public Task<DocumentSnapshot> getUser(String id){
        return mColection.document(id).get();
    }

    public Task<Void> Create(User user){
        return mColection.document(user.getId()).set(user);
    }
    public Task<Void> UpDate(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("username", user.getUserName());
        return mColection.document(user.getId()).update(map);
    }
}
