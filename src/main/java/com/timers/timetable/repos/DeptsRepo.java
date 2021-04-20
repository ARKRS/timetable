package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.users.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeptsRepo extends CrudRepository<Department, Long>{

    Department findByDeptname(String deptname);

    Department findTop1BySupervisor(User supervisor);

    Department findByExtCode(String extcode);

    Department findByExtCodeAndDeptname(String extcode, String deptname);

    List<Department> findAllByExtCodeIsNotNullOrderByDeptname();

//    @Query(value = "select parents.id,\n" +
//            "       coalesce(parents.parent,0) as parent_id\n" +
//            "\n" +
//            "    from department as parents\n" +
//            "\n" +
//            "where parents.id in (\n" +
//            "    select distinct depts.parent as id\n" +
//            "\n" +
//            "    from department as depts\n" +
//            "\n" +
//            "    where depts.parent is not null\n" +
//            "\n" +
//            "        union distinct\n" +
//            "\n" +
//            "    select distinct depts2.id as id\n" +
//            "\n" +
//            "    from department as depts2\n" +
//            "\n" +
//            "    where depts2.parent is null\n" +
//            "    )\n" +
//            "\n" +
//            "order by parents.deptname", nativeQuery = true)
//            List<Object[]> findAllParents();
//
//    @Query(value = "\n" +
//            "\n" +
//            "\n" +
//            "/* Запрос выдает родителей и подчиненных нормально */\n" +
//            "\n" +
//            "select        coalesce(children.id,0) as id,\n" +
//            "              parents.id as parent_id\n" +
//            "from department as parents\n" +
//            "left join department as children on parents.id = children.parent\n" +
//            "where parents.id in (\n" +
//            "                select distinct depts.parent\n" +
//            "                from department as depts\n" +
//            "                where depts.parent is not null\n" +
//            "\n" +
//            "                union all\n" +
//            "\n" +
//            "                select distinct depts2.id\n" +
//            "                    from department as depts2\n" +
//            "                where depts2.parent is null);\n" +
//            "\n",nativeQuery = true)
//    List<Object[]> findAllChildren();

    List<Department> getByIsActive(boolean active);

    @Query(value = "WITH RECURSIVE temp1 AS (\n" +
            "    SELECT fr1.parent, fr1.id, fr1.deptname, 1 as level,'/0/' as path\n" +
            "    FROM department fr1 WHERE fr1.parent is null\n" +
            "    union\n" +
            "    select fr2.parent, fr2.id, fr2.deptname, level+1 as level,\n" +
            "           temp1.path||fr2.parent||'/' path\n" +
            "    FROM department fr2\n" +
            "             INNER JOIN temp1\n" +
            "                        ON (temp1.id = fr2.parent and fr2.parent is not null\n" +
            "                            and temp1.path not like '%/'||fr2.id||'/%')\n" +
            "    where level < 6\n" +
            ")\n" +
            "\n" +
            "SELECT\n" +
            "    linear1.parent as parent,\n" +
            "    linear1.id as id,\n" +
            "    linear1.deptname as deptname,\n" +
            "    linear1.level as level,\n" +
            "    linear1.path as path,\n" +
            "    linear1.path||linear1.id||'/' as fullpath,\n" +
            "    count(d.id) as counter\n" +
            "FROM temp1 as linear1\n" +
            "         left join department d on linear1.id = d.parent\n" +
            "\n" +
            "group by linear1.parent,\n" +
            "         linear1.id,\n" +
            "         linear1.deptname,\n" +
            "         linear1.level,\n" +
            "         linear1.path,\n" +
            "         fullpath\n" +
            "\n" +
            "order by fullpath,linear1.deptname",
            nativeQuery = true)
    List<Object[]> getHiearchyDeptslist();
}



