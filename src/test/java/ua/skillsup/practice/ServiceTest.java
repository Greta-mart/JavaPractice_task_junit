package ua.skillsup.practice;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;

public class ServiceTest {
    @Mock
    private ExampleDao exampleDao;



    private Service service;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        service = new Service(exampleDao);
    }

    @DisplayName("Fail on null title and price")
    @org.junit.Test
    public void test1_addNewItem() {
        service.addNewItem(null, null);
        verify(exampleDao, Mockito.times(0)).store(Mockito.any(ExampleEntity.class));
    }

    @DisplayName("Fail on nullTitle")
    @org.junit.Test
    public void test2_addNewItem() {
        service.addNewItem(null, new BigDecimal(40.00));
        verify(exampleDao, Mockito.times(0)).store(Mockito.any(ExampleEntity.class));
    }

    @DisplayName("Fail on null price")
    @org.junit.Test
    public void test3_addNewItem() {
        service.addNewItem("Str", null);
        verify(exampleDao, Mockito.times(0)).store(Mockito.any(ExampleEntity.class));
    }

    @DisplayName("Fail on title less three symbols")
    @org.junit.Test
    public void test4_addNewItem() {
        service.addNewItem("St", null);
        verify(exampleDao, Mockito.times(0)).store(Mockito.any(ExampleEntity.class));
    }

    @DisplayName("Fail on title more 20 symbols")
    @org.junit.Test
    public void test5_addNewItem() {
        service.addNewItem("Titletitletitletitletitle", null);
        verify(exampleDao, Mockito.times(0)).store(Mockito.any(ExampleEntity.class));
    }

    @DisplayName("Fail on price less than limit")
    @org.junit.Test
    public void test6_addNewItem() {
        service.addNewItem("Title", new BigDecimal(4.00));
        verify(exampleDao, Mockito.times(0)).store(Mockito.any(ExampleEntity.class));
    }

    @DisplayName("Fail on anunique title")
    @org.junit.Test
    public void test7_getStatistic() {
        BigDecimal price = BigDecimal.valueOf(3000);
        List<ExampleEntity> existent = new ArrayList<>();
        ExampleEntity ee1 = new ExampleEntity();
        ee1.setTitle("title1");
        existent.add(ee1);
        ExampleEntity ee2 = new ExampleEntity();
        ee2.setTitle("title2");
        existent.add(ee2);
        ExampleEntity ee3 = new ExampleEntity();
        ee3.setTitle("title3");
        existent.add(ee3);

        Mockito.when(exampleDao.findAll()).thenReturn(existent);

        service.addNewItem("title1", price);
        service.addNewItem("title2", price);
        service.addNewItem("title3", price);

        Mockito.verify(exampleDao, Mockito.times(0)).store(Mockito.any(ExampleEntity.class));
    }

    @DisplayName("Fail on empty list")
    @org.junit.Test
    public void test8_getStatistic() {
        Mockito.when(exampleDao.findAll()).thenReturn(Collections.emptyList());

        Map<LocalDate, BigDecimal> actual = service.getStatistic();

        assertTrue(actual.isEmpty());
    }

   /* @org.junit.Test
    public void testAddNewItem_correct() {
        final String title = "Title";
        final BigDecimal price = new BigDecimal(30);
        service.addNewItem("Title", new BigDecimal(30));

        ExampleEntity ee = new ExampleEntity();
        ee.setPrice(new BigDecimal(30));
        ee.setTitle("Title");

        verify(exampleDao, Mockito.times(1)).store(Mockito.eq(ee));
    }*/


    @org.junit.Test
    public void test_getStatistic() {
        final LocalDate localDate08032018 = LocalDate.of(2018, Month.MARCH, 8);
        final LocalDate localDate18032018 = LocalDate.of(2018, Month.MARCH, 18);

        BigDecimal price1 = BigDecimal.valueOf(100);
        BigDecimal price2 = BigDecimal.valueOf(200);
        BigDecimal price3 = BigDecimal.valueOf(30);

        List<ExampleEntity> existent = new ArrayList<>();
        ExampleEntity ee1 = new ExampleEntity();
        ee1.setTitle("title1");
        ee1.setDateIn(ZonedDateTime.of(localDate08032018, LocalTime.MIN, ZoneId.systemDefault()).toInstant());
        ee1.setPrice(price1);
        existent.add(ee1);

        ExampleEntity ee2 = new ExampleEntity();
        ee2.setTitle("title2");
        ee2.setDateIn(ZonedDateTime.of(localDate08032018, LocalTime.MIN, ZoneId.systemDefault()).toInstant());
        ee2.setPrice(price2);
        existent.add(ee2);

        ExampleEntity ee3 = new ExampleEntity();
        ee3.setTitle("title3");
        ee3.setDateIn(ZonedDateTime.of(localDate18032018, LocalTime.MIN, ZoneId.systemDefault()).toInstant());
        ee3.setPrice(price3);
        existent.add(ee3);

        Mockito.when(exampleDao.findAll()).thenReturn(existent);

        Map<LocalDate, BigDecimal> actual = service.getStatistic();

        assertFalse(actual.isEmpty());
        assertTrue(actual.containsKey(localDate08032018));
        assertTrue(actual.get(localDate08032018).compareTo(price1.add(price2).divide(BigDecimal.valueOf(2))) == 0);

        assertTrue(actual.containsKey(localDate18032018));
        assertTrue(actual.get(localDate18032018).compareTo(price3) == 0);
    }
}