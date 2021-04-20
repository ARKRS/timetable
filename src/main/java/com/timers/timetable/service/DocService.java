package com.timers.timetable.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.docs.DepartmentDoc;
import com.timers.timetable.docs.DepartmentDocAdapter;
import com.timers.timetable.docs.FuelDoc;
import com.timers.timetable.docs.FuelDocAdapter;
import com.timers.timetable.repos.DocsRepo;
import com.timers.timetable.repos.FuelDocRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DocService {

    @Autowired
    private DocsRepo docsRepo;

    public String getDocs(DocsRepo docsRepo) {

        GsonBuilder gsonBuilder = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(DepartmentDoc.class, new DepartmentDocAdapter());

        Gson gson = gsonBuilder
                .create();

        List<DepartmentDoc> result = new ArrayList<>();

        Date date = getDateMinusMonth();

        List<DepartmentDoc> docList = docsRepo.findAllByDocUploaded(false);

        if (docList.size() > 0) {

        }

        for (DepartmentDoc doc : docList
        ) {

            result.add(doc);//gson.toJson(doc));
        }

        return gson.toJson(result);

    }

    public String getFuelDocs(FuelDocRepo docsRepo) {

        GsonBuilder gsonBuilder = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(FuelDoc.class, new FuelDocAdapter());

        Gson gson = gsonBuilder
                .create();

        List<FuelDoc> result = new ArrayList<>();

        Date date = getDateMinusMonth();

        List<FuelDoc> docList = docsRepo.findAllByDocUploaded(false);



        for (FuelDoc doc : docList
        ) {

            result.add(doc);//gson.toJson(doc));
        }

        return gson.toJson(result);

    }


    private Date getDateMinusMonth() {

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        date.setTime(calendar.getTime().getTime());
        return date;
    }

    public Map<Department,DepartmentDoc> getDocMapWithDepartments(Date workdate){

        List<DepartmentDoc> docs = docsRepo.findAllByWorkdate(workdate);
        HashMap<Department,DepartmentDoc> docsmap = new HashMap<>();
        for (DepartmentDoc doc : docs
             ) {

            docsmap.put(doc.getDepartment(),doc);
        }

        return docsmap;
    }


}

