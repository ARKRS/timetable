package com.timers.timetable.repos;

import com.timers.timetable.deptsmanagement.Department;
import com.timers.timetable.users.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeptsRepo extends CrudRepository<Department, Long> {

    Department findByDeptname(String deptname);

    Department findBySupervisor(User supervisor);

    Department findByExtCode(String extcode);

    Department findByExtCodeAndDeptname(String extcode, String deptname);

    List<Department> findAllByExtCodeIsNotNullOrderByDeptname();

    @Query(value = "select parents.id,\n" +
            "       coalesce(parents.parent,0) as parent_id\n" +
            "\n" +
            "    from department as parents\n" +
            "\n" +
            "where parents.id in (\n" +
            "    select distinct depts.parent as id\n" +
            "\n" +
            "    from department as depts\n" +
            "\n" +
            "    where depts.parent is not null\n" +
            "\n" +
            "        union distinct\n" +
            "\n" +
            "    select distinct depts2.id as id\n" +
            "\n" +
            "    from department as depts2\n" +
            "\n" +
            "    where depts2.parent is null\n" +
            "    )\n" +
            "\n" +
            "order by parents.deptname", nativeQuery = true)
            List<Object[]> findAllParents();

    @Query(value = "\n" +
            "\n" +
            "\n" +
            "/* Запрос выдает родителей и подчиненных нормально */\n" +
            "\n" +
            "select        coalesce(children.id,0) as id,\n" +
            "              parents.id as parent_id\n" +
            "from department as parents\n" +
            "left join department as children on parents.id = children.parent\n" +
            "where parents.id in (\n" +
            "                select distinct depts.parent\n" +
            "                from department as depts\n" +
            "                where depts.parent is not null\n" +
            "\n" +
            "                union all\n" +
            "\n" +
            "                select distinct depts2.id\n" +
            "                    from department as depts2\n" +
            "                where depts2.parent is null);\n" +
            "\n",nativeQuery = true)
    List<Object[]> findAllChildren();
}



