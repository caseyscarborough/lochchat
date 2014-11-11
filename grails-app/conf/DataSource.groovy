dataSource {
  pooled = true
  jmxExport = true
  driverClassName = "com.mysql.jdbc.Driver"
  dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
  username = "root"
  password = ""
  dbCreate = "none"
}

hibernate {
  cache.use_second_level_cache = true
  cache.use_query_cache = false
  cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
  singleSession = true // configure OSIV singleSession mode
}

environments {
  development {
    dataSource {
      url = "jdbc:mysql://localhost:3306/lochchat_dev?useUnicode=yes&characterEncoding=UTF-8"
    }
  }
  test {
    dataSource {
      url = "jdbc:mysql://localhost:3306/lochchat_test?useUnicode=yes&characterEncoding=UTF-8"
    }
  }
  production {
    dataSource {
      url = "jdbc:mysql://localhost:3306/lochchat_prod?useUnicode=yes&characterEncoding=UTF-8"
      properties {
        // See http://grails.org/doc/latest/guide/conf.html#dataSource for documentation
        jmxEnabled = true
        initialSize = 5
        maxActive = 50
        minIdle = 5
        maxIdle = 25
        maxWait = 10000
        maxAge = 10 * 60000
        timeBetweenEvictionRunsMillis = 5000
        minEvictableIdleTimeMillis = 60000
        validationQuery = "SELECT 1"
        validationQueryTimeout = 3
        validationInterval = 15000
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = false
        jdbcInterceptors = "ConnectionState"
        defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
      }
    }
  }
}