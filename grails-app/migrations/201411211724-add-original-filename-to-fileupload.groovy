databaseChangeLog = {

  changeSet(author: "casey (generated)", id: "1416695068412-1") {
    addColumn(tableName: "file_upload") {
      column(name: "original_filename", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }
}
