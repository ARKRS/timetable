package com.timers.timetable.docs;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.deptsmanagement.DeptWrapper;
import com.timers.timetable.employees.Employee;
import com.timers.timetable.employees.EmployeeStatus;
import com.timers.timetable.repos.*;
import com.timers.timetable.service.DeptService;
import com.timers.timetable.service.DocService;
import com.timers.timetable.service.UserService;
import com.timers.timetable.statics.ParameterFiller;
import com.timers.timetable.users.User;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/docs")
public class DocController {
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private DeptsRepo deptsRepo;
    @Autowired
    private DocsRepo docsRepo;
    @Autowired
    private FuelDocRepo fuelDocRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private DeptService deptService;
    @Autowired
    private DocService docService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private AbsentPeriodRepo absentPeriodRepo;
    //Подготовка вывода табеля на указанный месяц
    @PostMapping("/table")
    public String showTable(Model model, @RequestParam Map<String,String> form){
        Date month = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
           month = sdf.parse(form.get("date2table").toString());
        }
        catch (Exception e){
            return "hello";
        }

        LocalDate endOfmonthLd = month.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(1).withDayOfMonth(1).minusDays(1);
        Date endOfmonth =  java.util.Date.from(endOfmonthLd.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Department curdept = deptsRepo.findByExtCode(form.get("dept2table"));
        List<DepartmentDoc> docList = docsRepo.findAllByWorkdateBetweenAndDepartmentEquals(month, endOfmonth,curdept);

        HashMap<String, HashMap> dates = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyy");
        ArrayList<String> datelist = new ArrayList<>();
        Date it = month;
        while (it.getTime()<=endOfmonth.getTime()){


            dates.put(formatter.format(it), new HashMap());
            datelist.add(formatter.format(it));
            it = new Date(it.getTime() + 86400000);
        }

        ArrayList<String> employees = new ArrayList<>();
        HashMap<String,String> status_colors = new HashMap<>();

        for (DepartmentDoc doc: docList
             ) {

            HashMap<String,String> datemap=  new HashMap<>();//dates.get(doc.getWorkdate());

            for (Map.Entry<Employee, DepartmentDoc.AbsentPeriod> str: doc.getAbsentPeriods().entrySet()
                 ) {
                String sh_stat = str.getValue().employeeStatus.toShortString();
                datemap.put(str.getKey().getFio(),sh_stat);
                if (!employees.contains(str.getKey().getFio())){
                    employees.add(str.getKey().getFio());
                }
            }
            dates.put(formatter.format(doc.getWorkdate()),datemap);
        }

        for (EmployeeStatus status : EmployeeStatus.values()){
            status_colors.put(status.toShortString(),status.getColor());
        }
        Collections.sort(employees);
        model.addAttribute("employees",employees);
        model.addAttribute("table",dates);
        model.addAttribute("dates", datelist);
        model.addAttribute("dapartment_name", curdept.getDeptname());
        model.addAttribute("month", new SimpleDateFormat("LLLL yyyy г.").format(month));
        model.addAttribute("status_colors", status_colors);

        return "table";
    }

    @PostMapping("/bigtable")
    public String showBigTable(Model model, @RequestParam Map<String,String> form){

        List<DeptWrapper> deptlist = deptService.getDeptsHierarchy();
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(form.get("today"));
        }
        catch (ParseException e){

        }
        HashMap<Employee, DepartmentDoc.AbsentPeriod> employees = new HashMap<>();
        Map<Department,DepartmentDoc> docMap = docService.getDocMapWithDepartments(date);
        for ( Map.Entry<Department,DepartmentDoc> entry: docMap.entrySet()
             ) {

            for (Map.Entry<Employee, DepartmentDoc.AbsentPeriod> emp: entry.getValue().getAbsentPeriods().entrySet()
                 ) {
                employees.put(emp.getKey(),emp.getValue());
            }
        }
        Map<Department,List<Employee>> deptmapwithemps = deptService.getDepartmentMapWithEmployees();

        model.addAttribute("deptlist", deptlist);

        model.addAttribute("docmap",docMap);

        model.addAttribute("deptmapwithemps",deptmapwithemps);

        model.addAttribute("today",date);

        model.addAttribute("employees",employees);
        return "tablebig";
    }

    @PostMapping("/fuel")
    public String showFuelTable(Model model, @RequestParam Map<String,String> form){

        Date month = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        try {
            month = sdf.parse(form.get("date2fueltable").toString());
        }
        catch (Exception e){
            return "hello";
        }

        LocalDate endOfmonthLd = month.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusMonths(1).withDayOfMonth(1).minusDays(1);
        Date endOfmonth =  java.util.Date.from(endOfmonthLd.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Department curdept = deptsRepo.findByExtCode(form.get("dept2table"));
        List<FuelDoc> docList = fuelDocRepo.findAllByWorkmonthBetweenAndDepartmentEquals(month, endOfmonth,curdept);

        model.addAttribute("start_month",month);
        model.addAttribute("end_month", endOfmonth);
        HashMap<String, HashMap> dates = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyy");
        ArrayList<String> datelist = new ArrayList<>();
        Date it = month;

        while (it.getTime()<endOfmonth.getTime()){
            dates.put(formatter.format(it), new HashMap());
            datelist.add(formatter.format(it));
            it = new Date(it.getTime() + 86400000);
        }

        ArrayList<String> employees = new ArrayList<>();

        List<Employee> employees1 =  employeeRepo.findAllByDepartment(curdept);

        HashMap<String,String> emplist = new HashMap<>();

        HashMap<String,String> carInfo = new HashMap<>();

        for (Employee emp: employees1
             ) {
            String empCode = String.format("%08d",emp.getEmployee_id());
            employees.add(emp.getFio());
            emplist.put(emp.getFio(),empCode);

            if (emp.getCarModel()!=null){
                carInfo.put("car_model_" + empCode, emp.getCarModel());
            }
            if (emp.getCarNumber()!=null){
                carInfo.put("car_number_" + empCode, emp.getCarNumber());
            }
            if (emp.getCarConsumption()!=null){
                carInfo.put("car_consumption_" + empCode, emp.getCarConsumption().toString());
            }

            if (emp.getFuelType()!=null){
                carInfo.put("car_fueltype_"+empCode, emp.getFuelType().toString());
            }
        }

        Collections.sort(employees);
        //* formatted date <empcode,amount> *//
        Map<String,Float> docdetais = new TreeMap<>();
        Map<String,Date>  docdetais_dates = new TreeMap<>();

        List<String> stored_details = new ArrayList<>();
        for (FuelDoc doc: docList) {

            for (FuelDocDetails fdd : doc.getFuelDocDetails()
                 ) {
                //восьмизначный код сотрудника + форматированная дата
                String empcode = String.format("%08d",fdd.getEmployee().getEmployee_id());
                //добавил еще один хашмап, чтобы хранить там даты. Не получилось в тимлифе конвертировать строку в дату.
                docdetais.put(empcode+"@"+formatter.format(fdd.getDateOfMonth()),fdd.getAmount());
                docdetais_dates.put(empcode+"@"+formatter.format(fdd.getDateOfMonth()),fdd.getDateOfMonth());

                if (!stored_details.contains(empcode)){
                    stored_details.add(empcode);
                }

            }
            for (FuelDocCarDetails fdd : doc.getFuelDocCarDetails()){

               carInfo.put("odo_start_" + String.format("%08d",fdd.getEmployee().getEmployee_id()), String.valueOf(fdd.getStartOdometerData()));
               carInfo.put("odo_end_" + String.format("%08d",fdd.getEmployee().getEmployee_id()), String.valueOf(fdd.getEndOdometerData()));

               carInfo.put("tankreststart_" + String.format("%08d",fdd.getEmployee().getEmployee_id()), String.valueOf(fdd.getTankreststart()));
               carInfo.put("tankrestend_" + String.format("%08d",fdd.getEmployee().getEmployee_id()), String.valueOf(fdd.getTankreststart()));

               carInfo.put("fueltype_" + String.format("%08d",fdd.getEmployee().getEmployee_id()), String.valueOf(fdd.getFuelType()));

            }
        }

        model.addAttribute("carInfo",carInfo);
        model.addAttribute("docdetais",docdetais);
        model.addAttribute("dates",datelist);
        model.addAttribute("employees",employees);
        model.addAttribute("emplist",emplist);
        model.addAttribute("table",dates);
        model.addAttribute("department_name", curdept.getDeptname());
        model.addAttribute("department_id", curdept.getId());
        model.addAttribute("month", new SimpleDateFormat("LLLL yyyy г.").format(month));
        model.addAttribute("stored_details",stored_details);
        model.addAttribute("docdetais_dates", docdetais_dates);

        return "fueltable";

    }

    @PostMapping("/savefueldoc")
    public String savefuelDoc(@RequestParam Map<String, String> form, Model model) throws ParseException {

        Optional departmentOptional = deptsRepo.findById(Long.parseLong(form.get("department_id")));

        Date workdate;
        Calendar calendar = Calendar.getInstance();
        try {
            workdate = new SimpleDateFormat("LLLL yyyy г.").parse(form.get("workmonth"));
        } catch (ParseException e) {
            return "hello";
        }

        Department department = (Department) departmentOptional.get();
        FuelDoc curDoc = fuelDocRepo.findByDepartmentAndWorkmonth(department, workdate);

        if (curDoc == null) {
            curDoc = new FuelDoc();
            curDoc.setDocUploaded(false);
            curDoc.setDepartment(department);
            curDoc.setWorkmonth(workdate);
            curDoc.setDoc_UUID(UUID.randomUUID().toString());


        } else {
            if (curDoc.getDoc_UUID() == null) {
                curDoc.setDoc_UUID(UUID.randomUUID().toString());
            }
        }
        Map<String,Employee> employeeMap = new HashMap<>();
        Map<Employee,Map<Date,Float>> docdetails= new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (Map.Entry<String,String> el: form.entrySet()
             ) {

            if (el.getKey().contains("fuelamount")){

                if (!el.getValue().equals("")){

                    String empcode = el.getKey().substring(el.getKey().lastIndexOf("__")+2).substring(0,8);

                    String datecode  = form.get("fueldate__" + empcode + el.getKey().substring(el.getKey().lastIndexOf(empcode)+8));

                    /*String datecode = el.getKey().replace("__"+empcode,"");*/
                    datecode = datecode.substring(datecode.lastIndexOf("_")+1);
                    Employee employee = employeeMap.get(empcode);
                    if (employee==null){
                        employee = employeeRepo.findById(Long.parseLong(empcode)).get();
                        employeeMap.put(empcode,employee);
                    }

                    Map<Date,Float> empDetails = docdetails.get(employee);
                    if (empDetails==null){
                        docdetails.put(employee,new HashMap<>());
                        empDetails = docdetails.get(employee);
                    }

                    /*calendar.setTime(workdate);
                    calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(datecode));*/

                    empDetails.put(sdf.parse(datecode),Float.parseFloat(el.getValue()));

                    int j = 0;
                    j++;
                }
            }
        }

        ArrayList<FuelDocDetails> fddlist = new ArrayList<>();
        ArrayList<FuelDocCarDetails> docCarDetails = new ArrayList<>();


        for (Map.Entry<Employee,Map<Date,Float>> emp: docdetails.entrySet()
             ) {
            //--> Начало: Обрабатываем данные по автомобилю сотрудника
            Employee temp_emp = emp.getKey();
            String temp_emp_code = String.format("%08d",temp_emp.getEmployee_id());
            boolean empProcessed = false;

            if (!empProcessed) {
                String car_model = form.get("car_model_" + temp_emp_code);
                String car_number = form.get("car_number_" + temp_emp_code);
                String fuel_type  = form.get("fueltype_" + temp_emp_code);
                Integer car_consumption = 0;
                try {
                    car_consumption = Integer.parseInt(form.get("car_consumption_" + temp_emp_code));
                } catch (Exception e) {

                }

                if (car_model!=null && car_number!=null && car_consumption != 0) {

                    boolean needSaving = false;

                    if ((temp_emp.getCarModel()==null)||(!temp_emp.getCarModel().equals(car_model))) {
                        temp_emp.setCarModel(car_model);
                        needSaving = true;
                    }
                    if ((temp_emp.getCarNumber()==null)||(!temp_emp.getCarNumber().equals(car_number))) {
                        temp_emp.setCarNumber(car_number);
                        needSaving = true;
                    }
                    if (temp_emp.getCarConsumption()==null ||(!temp_emp.getCarConsumption().equals(car_consumption))) {
                        temp_emp.setCarConsumption(car_consumption);
                        needSaving = true;
                    }
                    if (temp_emp.getFuelType()==null || (temp_emp.getFuelType()!= FuelType.fromString(fuel_type))) {
                        temp_emp.setFuelType(FuelType.fromString(fuel_type));
                        needSaving = true;
                    }

                    if (needSaving)
                        employeeRepo.save(temp_emp);
                }
                empProcessed = true;
            }
            //--> Конец: Обрабатываем данные по автомобилю сотрудника

            for (Map.Entry<Date,Float> date_ : emp.getValue().entrySet()
                 ) {

                FuelDocDetails fdd = new FuelDocDetails(curDoc,emp.getKey(),date_.getKey(),date_.getValue());
                fddlist.add(fdd);
            }


        }

        for (Map.Entry<String,Employee> employeeEntry: employeeMap.entrySet()){

            String odo_start = form.get("odo_start_"+ String.format("%08d",employeeEntry.getValue().getEmployee_id()));
            int odostart;
            if (odo_start.equals("")){
                odostart = 0;
            }
            else {
                odostart = Integer.parseInt(odo_start);
            }
            String odo_end = form.get("odo_end_"+ String.format("%08d",employeeEntry.getValue().getEmployee_id()));
            int odoend;
            if (odo_end.equals("")){
                odoend = 0;
            }
            else {
                odoend = Integer.parseInt(odo_end);
            }

            String tankreststart_ = form.get("tankreststart_"+ String.format("%08d",employeeEntry.getValue().getEmployee_id()));
            int tankreststart;
            if (tankreststart_.equals("")){
                tankreststart = 0;
            }
            else {
                tankreststart = Integer.parseInt(tankreststart_);
            }

            String tankrestend_ = form.get("tankrestend_"+ String.format("%08d",employeeEntry.getValue().getEmployee_id()));
            int tankrestend;
            if (tankrestend_.equals("")){
                tankrestend = 0;
            }
            else {
                tankrestend = Integer.parseInt(tankrestend_);
            }

            String fueltypestr = form.get("fueltype_"+ String.format("%08d",employeeEntry.getValue().getEmployee_id()));

            FuelType fueltype = FuelType.fromString(fueltypestr);



            docCarDetails.add(new FuelDocCarDetails(curDoc,
                    employeeEntry.getValue(),
                    odostart,
                    odoend,
                    tankreststart,
                    tankrestend,
                    fueltype));
        }
        curDoc.setFuelDocDetails(fddlist);

        curDoc.setFuelDocCarDetails(docCarDetails);
        if (curDoc.getFuelDocCarDetails().size()!=0 && curDoc.getFuelDocDetails().size()!=0)
            fuelDocRepo.save(curDoc);

        //return "redirect:/gotohello";

        ParameterFiller.fillModelParameters(model,userService,deptsRepo,deptService);

        if (model.getAttribute("isAdmin") == Boolean.TRUE){
            Department dept_ = deptsRepo.findById(Long.parseLong(form.get("department_id"))).get();
            if(dept_!=null) {
                model.addAttribute("department", dept_.getDeptname());
                model.addAttribute("departmentextcode",  dept_.getExtCode());
            }
            else {
                model.addAttribute("department", null);
                model.addAttribute("departmentextcode",  null);
            }
            model.addAttribute("startMonth", sdf.parse(form.get("start_month")));
        }
        return "hello";//"redirect:/gotohello";

    }


    @PostMapping
    public String createDoc(Model model, @RequestParam("dept2table") String dept2table, @RequestParam("today") String selectedDay) throws NotFoundException, ParseException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null)
            throw new NotFoundException("Not authority");
        Object obj = auth.getPrincipal();

        String username = "";

        if (obj instanceof UserDetails) {
            username = ((UserDetails) obj).getUsername();
        } else {
            username = obj.toString();
        }
        User curUser = userRepo.findByUsernameAndActive(username, true);
        Date selectedDay_ = new SimpleDateFormat("yyyy-MM-dd").parse(selectedDay);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyy");
        Date today = simpleDateFormat.parse(simpleDateFormat.format(selectedDay_));

        Department department = deptsRepo.findByExtCode(dept2table);

        if (department == null) {

            return "redirect:/gotohello";
        }

        List<Employee> employees = employeeRepo.findEmployeesByDepartment_IdOrderByFio(department.getId());

        DepartmentDoc curDoc = docsRepo.findByDepartmentAndWorkdate(department, today);

        DecimalFormat df = new DecimalFormat("0000");
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (Employee emp : employees
        ) {
            Map<String, Object> map1 = new HashMap<>();
            if (curDoc != null) {
/*
                Hibernate.initialize(curDoc.getAbsentPeriods());
                Hibernate.initialize(curDoc.getEmployees());
*/
                EmployeeStatus curStatus = curDoc.getEmployees().get(emp);
                DepartmentDoc.AbsentPeriod absentPeriod = curDoc.getAbsentPeriods().get(emp);
                if (curStatus != null) {
                    map1.put("employee", emp.getFio());
                    map1.put("status", absentPeriod.getEmployeeStatus());
                    map1.put("beginhour", absentPeriod.getBeginhour());
                    map1.put("beginminutes", absentPeriod.getBeginminutes());
                    map1.put("endhour", absentPeriod.getEndhour());
                    map1.put("endminutes", absentPeriod.getEndminutes());
                    map1.put("employee_index", df.format(emp.getEmployee_id()));
                } else {
                    map1.put("employee", emp.getFio());
                    map1.put("status", EmployeeStatus.UNDEF);
                    map1.put("beginhour", 0);
                    map1.put("beginminutes", 0);
                    map1.put("endhour", 0);
                    map1.put("endminutes", 0);
                    map1.put("employee_index", df.format(emp.getEmployee_id()));

                }

            } else {
                map1.put("employee", emp.getFio());
                map1.put("status", EmployeeStatus.UNDEF);
                map1.put("beginhour", 0);
                map1.put("beginminutes", 0);
                map1.put("endhour", 0);
                map1.put("endminutes", 0);
                map1.put("employee_index", df.format(emp.getEmployee_id()));
            }


            mapList.add(map1);
        }

        model.addAttribute("department_id", department.getId());
        model.addAttribute("dapartment_name", department.getDeptname());

        model.addAttribute("today", new SimpleDateFormat("dd.MM.yyy").format(today));
        model.addAttribute("username", curUser);
        model.addAttribute("employees", mapList);

        return "doceditor";
    }



    @PostMapping("/savedoc")
    public String saveDoc(@RequestParam Map<String, String> form) {

        Optional departmentOptional = deptsRepo.findById(Long.parseLong(form.get("department_id")));

        Date workdate;

        try {
            workdate = new SimpleDateFormat("dd.MM.yyy").parse(form.get("today"));
        } catch (ParseException e) {
            return "hello";
        }

        Department department = (Department) departmentOptional.get();
        DepartmentDoc curDoc = docsRepo.findByDepartmentAndWorkdate((Department) departmentOptional.get(), workdate);

        if (curDoc == null) {
            curDoc = new DepartmentDoc();
            curDoc.setDocUploaded(false);
            curDoc.setDepartment(department);
            curDoc.setWorkdate(workdate);
            curDoc.setDoc_UUID(UUID.randomUUID().toString());
            curDoc.setAbsentPeriods(new HashMap<>() {
            });

        } else {
            if (curDoc.getDoc_UUID() == null) {
                curDoc.setDoc_UUID(UUID.randomUUID().toString());
            }
        }

        Map<Employee, EmployeeStatus> employeeStatusMap = new HashMap<>();

        Map<Employee, DepartmentDoc.AbsentPeriod> absentPeriodMap = new HashMap<>();

        for (Map.Entry<String, String> val : form.entrySet()
        ) {

            if (val.getKey().contains("employeeStatus")) {

                String strEmpId = val.getKey().replace("employeeStatus", "");
                Long emp_id = Long.parseLong(strEmpId);

                Optional emp_optional = employeeRepo.findById(emp_id);
                Employee curemp = (Employee) emp_optional.get();
                EmployeeStatus employeeStatus = EmployeeStatus.fromRussian(val.getValue());
                employeeStatusMap.put(curemp, employeeStatus);
                int beginhour = 0;
                int beginminutes = 0;
                int endhour = 0;
                int endminutes = 0;

                //
                if (employeeStatus.equals(EmployeeStatus.PART_ABSENT) || employeeStatus.equals(EmployeeStatus.REMOTE)) {

                    beginhour = Integer.parseInt(form.get("beginhour_" + strEmpId));
                    beginminutes = Integer.parseInt(form.get("beginminutes_" + strEmpId));
                    endhour = Integer.parseInt(form.get("endhour_" + strEmpId));
                    endminutes = Integer.parseInt(form.get("endminutes_" + strEmpId));

                    //                absentPeriodMap.put(curemp, new AbsentPeriod(beginhour,beginminutes,endhour,endminutes));
                }
                absentPeriodMap.put(curemp, new DepartmentDoc.AbsentPeriod(beginhour, endhour, beginminutes, endminutes, employeeStatus));


            }
        }

        curDoc.setEmployees(employeeStatusMap);
        curDoc.setAbsentPeriods(absentPeriodMap);
        curDoc.setDocUploaded(false);
        docsRepo.save(curDoc);

        return "redirect:/gotohello";
    }

//    @GetMapping("/test")
//    public String testMethod(){
//
//        return "hello";
//    }

}
