databaseChangeLog = {

  changeSet(author: "Casey (generated)", id: "1416683801280-1") {
    createTable(tableName: "file_upload") {
      column(autoIncrement: "true", name: "id", type: "bigint") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "file_uploadPK")
      }

      column(name: "version", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "date_created", type: "datetime") {
        constraints(nullable: "false")
      }

      column(name: "filename", type: "varchar(255)") {
        constraints(nullable: "false")
      }

      column(name: "location", type: "varchar(255)") {
        constraints(nullable: "false")
      }
    }
  }
}
