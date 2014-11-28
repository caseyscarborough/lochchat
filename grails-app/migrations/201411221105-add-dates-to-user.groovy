databaseChangeLog = {

  changeSet(author: "casey (generated)", id: "1416758747313-1") {
    addColumn(tableName: "user") {
      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "casey (generated)", id: "1416758747313-2") {
    addColumn(tableName: "user") {
      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }
    }
  }
}
