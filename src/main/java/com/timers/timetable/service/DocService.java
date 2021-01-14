package com.timers.timetable.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timers.timetable.docs.DepartmentDoc;
import com.timers.timetable.docs.DepartmentDocAdapter;
import com.timers.timetable.repos.DocsRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DocService {

     public String getDocs(DocsRepo docsRepo){

        GsonBuilder gsonBuilder = new GsonBuilder()
           .setPrettyPrinting()
           .registerTypeAdapter(DepartmentDoc.class,new DepartmentDocAdapter());

        Gson gson = gsonBuilder
                .create();

         List<String>result = new ArrayList<>();

        Date date = getDateMinusMonth();

        List<DepartmentDoc> docList = docsRepo.findAllByDocUploaded(false);

        if (docList.size()>0){

        }

        for (DepartmentDoc doc: docList
        ) {

            result.add(gson.toJson(doc));
        }

        return gson.toJson(result);

    }

    private Date getDateMinusMonth(){

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,-1);
        date.setTime(calendar.getTime().getTime());
        return date;
    }


}

