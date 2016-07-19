package com.dhatim.sql.hamcrest;

import static org.hamcrest.Matchers.*;
import static com.dhatim.sql.hamcrest.QueryMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.Test;

public class QueryMatchersTest {

    private static SqlQuery sql(String sql) {
        return SqlQuery.of(sql);
    }
    
    @Test
    public void testSimpleFromTableName() {
        assertThat(sql("select id from mytable"), query(from(table(equalToIgnoringCase("mytable")))));
    }
    
    @Test
    public void testSimpleLeftJoin() {
        assertThat(sql("SELECT * FROM mytable m LEFT JOIN myothertable o ON my.id = o.outid"), query(from(table(equalToIgnoringCase("mytable")), leftJoin(equalToIgnoringCase("myothertable")))));
    }
    
    @Test
    public void testPosition() {
        assertThat(sql("SELECT POSITION('3' IN '123456')"), query(position(any(), literal("123456"))));
    }
    
    @Test
    public void testNot() {
        assertThat(sql("SELECT NOT funcName('hello')"), query(not(call(is("funcName"), any()))));
    }
    
    @Test
    public void testCast() {
        assertThat(sql("SELECT '5'::integer"), query(select(cast(literal("5"), "integer"))));
    }
    
    @Test
    public void testNullCast() {
        assertThat(sql("SELECT NULL::integer"), query(select(nullCast("integer"))));
    }
    
    @Test
    public void testJsonCast() {
        assertThat(sql("SELECT '{}'::jsonb"), query(select(cast(literal("{}"), "jsonb"))));
    }
    
    @Test
    public void testRowSelect() {
        assertThat(sql("SELECT 'E', (4, 'E')"), query(select(row(literal(4), literal("E")))));
    }
    
    @Test
    public void testRowWhere() {
        assertThat(sql("SELECT * FROM t1 WHERE (col1, col2) = (1, 2)"), query());
    }
    
    @Test
    public void testAnyUuidCast() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 = ANY('{}'::uuid[])"), query());
    }
    
    @Test
    public void testLike() {
        assertThat(sql("SELECT * FROM t1 WHERE col1 LIKE '%Lorem'"), query(where(like(column("col1"), literal("%Lorem")))));
    }
    
    @Test
    public void testUUIDLiteral() {
        assertThat(sql("SELECT UUID 'c96ff414-8559-484c-bd43-c978130a5ee4'"), query(uuidLiteral("c96ff414-8559-484c-bd43-c978130a5ee4")));
    }
    
    @Test
    public void testDateLiteral() {
        assertThat(sql("SELECT DATE '2010-10-10'"), query(dateLiteral("2010-10-10")));
    }
    
}
