package tk.mybatis.mapper.test.example;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.mapper.CountryMapper;
import tk.mybatis.mapper.mapper.MybatisHelper;
import tk.mybatis.mapper.model.Country;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuyi
 * @date 2017/11/18
 */
public class TestExampleBuilder {

    @Test
    public void testExampleBuilder() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class).build();
            List<Country> countries = mapper.selectByExample(example);
            Assert.assertEquals(183, countries.size());
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testEqualTo() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom().andEqualTo("id", "35"))
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Country country = countries.get(0);
            Assert.assertEquals(Integer.valueOf(35), country.getId());
            Assert.assertEquals("China", country.getCountryname());
            Assert.assertEquals("CN", country.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testBetween() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom().andBetween("id", 34, 35))
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Country country35 = countries.get(0);
            Assert.assertEquals(Integer.valueOf(35), country35.getId());
            Assert.assertEquals("China", country35.getCountryname());
            Assert.assertEquals("CN", country35.getCountrycode());

            Country country34 = countries.get(1);
            Assert.assertEquals(Integer.valueOf(34), country34.getId());
            Assert.assertEquals("Chile", country34.getCountryname());
            Assert.assertEquals("CL", country34.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testIn() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom().andIn("id", new ArrayList<Integer>(Arrays.asList(35, 183))))
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Country country35 = countries.get(1);
            Assert.assertEquals(Integer.valueOf(35), country35.getId());
            Assert.assertEquals("China", country35.getCountryname());
            Assert.assertEquals("CN", country35.getCountrycode());

            Country country183 = countries.get(0);
            Assert.assertEquals(Integer.valueOf(183), country183.getId());
            Assert.assertEquals("Zambia", country183.getCountryname());
            Assert.assertEquals("ZM", country183.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testCompound() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom()
                            .andEqualTo("countryname", "China")
                            .andEqualTo("id", 35)
                            .orIn("id", new ArrayList<Integer>(Arrays.asList(35, 183)))
                            .orLike("countryname","Ye%")
                    )
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Country country35 = countries.get(2);
            Assert.assertEquals(Integer.valueOf(35), country35.getId());
            Assert.assertEquals("China", country35.getCountryname());
            Assert.assertEquals("CN", country35.getCountrycode());

            Country country183 = countries.get(0);
            Assert.assertEquals(Integer.valueOf(183), country183.getId());
            Assert.assertEquals("Zambia", country183.getCountryname());
            Assert.assertEquals("ZM", country183.getCountrycode());

            Country country179 = countries.get(1);
            Assert.assertEquals(Integer.valueOf(179), country179.getId());
            Assert.assertEquals("Yemen", country179.getCountryname());
            Assert.assertEquals("YE", country179.getCountrycode());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testWhereAndWhereCompound() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom()
                            .andEqualTo("countryname", "China")
                            .andEqualTo("id", 35)
                    )
                    .andWhere(Sqls.custom()
                                .andEqualTo("id", 183)
                    )
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assert.assertEquals(0, countries.size());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testWhereOrWhereCompound() {
        SqlSession sqlSession = MybatisHelper.getSqlSession();
        try {
            CountryMapper mapper = sqlSession.getMapper(CountryMapper.class);
            Example example = Example.builder(Country.class)
                    .where(Sqls.custom()
                            .andEqualTo("countryname", "China")
                            .andEqualTo("id", 35)
                    )
                    .orWhere(Sqls.custom()
                            .andEqualTo("id", 183)
                    )
                    .build();
            List<Country> countries = mapper.selectByExample(example);
            Assert.assertEquals(2, countries.size());

        } finally {
            sqlSession.close();
        }
    }
}
