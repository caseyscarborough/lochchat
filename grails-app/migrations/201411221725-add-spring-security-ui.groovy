databaseChangeLog = {

  changeSet(author: "casey (generated)", id: "1416781550594-1") {
    createTable(tableName: "registration_code") {
      column(autoIncrement: "true", name: "id", type: "bigint") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "registration_PK")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "token", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "username", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }
}
