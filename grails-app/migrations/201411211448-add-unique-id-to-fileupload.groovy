databaseChangeLog = {

  changeSet(author: "Casey (generated)", id: "1416685734477-1") {
    addColumn(tableName: "file_upload") {
      column(name: "unique_id", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }
}
