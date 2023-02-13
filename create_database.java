import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import javax.sound.sampled.SourceDataLine;

public class create_database {

    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5433/postgres";

    static final String USER = "postgres";
    static final String PASS = "iitropar";

    public static void main(String[] args) {

        // String query1 = "create database db_sample";
        String query2 = "create table tickets(pnr int primary key,no_of_psng int ,train_no int , doj varchar(20), names text[], seat text[], type varchar(2))";
        String query6 = "create table train_avail (train_no int primary key )";
        String query3 = "create table trains ( train_no int  , doj varchar(12) , ac_coaches int , sl_coaches int, primary key (train_no , doj))";
        String query4 = "create table ac_coach_comp (berth_no int , type varchar(2))";
        String query5 = "create table sl_coach_comp (berth_no int , type varchar(2))";
       

        //////////// fucntions query ///////////
        String query8 = "CREATE OR REPLACE FUNCTION bookkrlo(psng int , t_no int , dte varchar , names text[],coach_type varchar, res out int)RETURNS int AS $$ declare cnt int;brth varchar;strt_seat_no int;end_seat_no int;total_ac_seats int;total_sl_seats int;v int;cnt_seat int;t_pnr int;old_pnr int;seats text[];final_seat varchar;ans varchar;ans2 varchar;tempo int ;BEGIN cnt:=0;cnt_seat:=0;total_ac_seats:=(select ac_coaches from trains t where t.doj=dte and t.train_no=t_no);total_sl_seats:=(select sl_coaches from trains t where t.doj=dte and t.train_no=t_no);total_ac_seats:=total_ac_seats*18;total_sl_seats:=total_sl_seats*24;if coach_type='AC' then cnt_seat:=(select sum(no_of_psng) from tickets t where t.doj=dte and t.train_no=t_no and t.type='AC') ;if cnt_seat>=total_ac_seats then res=0;return; end if;if cnt_seat is NULL then cnt_seat=0; raise notice '% ','cnt_seat'; end if;cnt_seat:=(total_ac_seats-cnt_seat);else cnt_seat:= (select sum(no_of_psng) from tickets t where t.doj=dte and t.train_no=t_no and t.type='SL') ;if cnt_seat>=total_sl_seats then res=0;return; end if;if cnt_seat is NULL then cnt_seat=0; end if;cnt_seat:=total_sl_seats-cnt_seat;end if;If coach_type='AC' then strt_seat_no:=total_ac_seats-cnt_seat+1;end_seat_no:=strt_seat_no + psng;else strt_seat_no:=total_sl_seats-cnt_seat+1;end_seat_no=strt_seat_no + psng;end if;FOR cur IN 1..psng loop t_pnr:=(select random()*(999999999-100000000)+100000000);old_pnr:=(select count(*) from tickets t where t.pnr=t_pnr );while old_pnr>0 loop t_pnr:=(select random()*(999999999-100000000)+100000000);old_pnr:=(select count(*) from tickets t where t.pnr=t_pnr );end loop; If coach_type='AC' then If strt_seat_no%18=0 then brth:= (Select type from ac_coach_comp  where berth_no=18);ans:=CAST (18 AS varchar);ans2:=CAST (strt_seat_no/18 AS varchar);v:=total_ac_seats/18;if (strt_seat_no/18) >v then  res=0;return; end if;final_seat:=Concat('(',ans,'/',brth,'/','C-',ans2,')');strt_seat_no:=strt_seat_no+1;else brth:=(select type from ac_coach_comp where berth_no=strt_seat_no%18);ans:=CAST (strt_seat_no%18 AS varchar);ans2:=CAST (strt_seat_no/18+1 AS varchar);v:=total_ac_seats/18;if (strt_seat_no/18 +1) >v then  res=0;return; end if;final_seat:=Concat('(',ans,'/',brth,'/','C-',ans2,')');strt_seat_no:=strt_seat_no+1;End if;else If strt_seat_no%24=0 then brth:= (Select type from sl_coach_comp  where berth_no=24);ans:=CAST (24 AS varchar);ans2:=CAST (strt_seat_no/24 AS varchar);v:=total_sl_seats/24;if (strt_seat_no/24) >v then  res=0;return; end if;final_seat:=Concat('(',ans,'/',brth,'/','C-',ans2,')');strt_seat_no=strt_seat_no+1;else brth:= (select type from sl_coach_comp where berth_no=strt_seat_no%24);ans:=CAST (strt_seat_no%24 AS varchar);ans2:=CAST (strt_seat_no/24+1 AS varchar);v:=total_sl_seats/18;if (strt_seat_no/24 + 1) >v then  res=0;return; end if;final_seat:=Concat('(',ans,'/',brth,'/','C-',ans2,')');strt_seat_no=strt_seat_no+1;end if;end if;seats[cnt]:=final_seat;raise notice '%',final_seat;cnt:=cnt+1;End loop;res:=t_pnr;insert into tickets values (t_pnr,psng,t_no, dte , names,seats,coach_type);End;$$ language plpgsql;";
        String query9 = "create or replace function release_train(t_no int,dt varchar,ac int ,sl int,res out varchar) RETURNS varchar AS $$ declare cnt int;begin cnt:=(select count(*) from train_avail t where t.train_no=t_no);if cnt>0 then insert into trains values(t_no,dt,ac,sl);res='yes';else res='no';end if;end;$$language plpgsql";
        String query10 = "create or replace function enter_train(t_no int) RETURNS VOID AS $$ declare res text;begin insert into train_avail values(t_no);end $$language plpgsql";

        ///////////// COACH COMPOSITION////////////

        String query11 = "insert into ac_coach_comp  values(1,'LB');insert into ac_coach_comp  values(2,'LB');insert into ac_coach_comp  values(3,'UB');insert into ac_coach_comp  values(4,'UB');insert into ac_coach_comp  values(5,'SL');insert into ac_coach_comp  values(6,'SU');insert into ac_coach_comp  values(7,'LB');insert into ac_coach_comp  values(8,'LB');insert into ac_coach_comp  values(9,'UB');insert into ac_coach_comp  values(10,'UB');insert into ac_coach_comp  values(11,'SL');insert into ac_coach_comp  values(12,'SU');insert into ac_coach_comp  values(13,'LB');insert into ac_coach_comp  values(14,'LB');insert into ac_coach_comp  values(15,'UB');insert into ac_coach_comp  values(16,'UB');insert into ac_coach_comp  values(17,'SL');insert into ac_coach_comp  values(18,'SU')";
        String query12 = "insert into sl_coach_comp values(1,'LB');insert into sl_coach_comp values(2,'MB');insert into sl_coach_comp values(3,'UB');insert into sl_coach_comp values(4,'LB');insert into sl_coach_comp values(5,'MB');insert into sl_coach_comp values(6,'UB');insert into sl_coach_comp values(7,'SL');insert into sl_coach_comp values(8,'SU');insert into sl_coach_comp values(9,'LB');insert into sl_coach_comp values(10,'MB');insert into sl_coach_comp values(11,'UB');insert into sl_coach_comp values(12,'LB');insert into sl_coach_comp values(13,'MB');insert into sl_coach_comp values(14,'UB');insert into sl_coach_comp values(15,'SL');insert into sl_coach_comp values(16,'SU');insert into sl_coach_comp values(17,'LB');insert into sl_coach_comp values(18,'MB');insert into sl_coach_comp values(19,'UB');insert into sl_coach_comp values(20,'LB');insert into sl_coach_comp values(21,'MB');insert into sl_coach_comp values(22,'UB');insert into sl_coach_comp values(23,'SL');insert into sl_coach_comp values(24,'SU')";


        ///////////////SETTING ISOLATION LEVEL ////////////
        String query13="alter database postgres SET DEFAULT_TRANSACTION_ISOLATION TO 'SERIALIZABLE'"; 
        Connection c = null;
        Statement stmt = null;

        try {

            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query2);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query6);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query4);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query5);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query3);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }

        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            stmt.executeUpdate(query8);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            stmt.executeUpdate(query9);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            stmt.executeUpdate(query10);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }

        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            stmt.executeUpdate(query11);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            stmt.executeUpdate(query12);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }

        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();
            stmt.executeUpdate(query13);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            // System.exit(0);
        }




        System.out.println("Databse Created Successfully.");

    }

}
