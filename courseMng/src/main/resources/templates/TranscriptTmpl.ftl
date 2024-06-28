<table border="1">
    <tr>
        <th>课程名</th>
        <th>得分</th>
        <th>学分</th>
        <th>绩点</th>
        <th>类型</th>
        <th>专业</th>
        <th>班级</th>
    </tr>
    <#list courseList as course>
        <tr>
            <td>${course.name}</td>
            <td>${course.scoreByUser}</td>
            <td>${course.creditsByUser}</td>
            <td>${course.pointByUser}</td>
            <td>
                <#if course.type == 1>
                    必修
                <#elseif course.type == 2>
                    选修
                <#else>
                    其他
                </#if>
            </td>
            <td>${course.profession}</td>
            <td>${course.grade}</td>
        </tr>
    </#list>
</table>
