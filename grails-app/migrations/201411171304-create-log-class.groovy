databaseChangeLog = {

  changeSet(author: "Casey", id: "create-log-class-1") {
    createTable(tableName: "log") {
      column(autoIncrement: "true", name: "id", type: "bigint") {
        constraints(nullable: "false", primaryKey: "true", primaryKeyName: "logPK")
      }

      column(name: "version", type: "bigint") {
        constraints(nullable: "false")
      }

      column(name: "contents", type: "longtext")

      column(name: "last_updated", type: "datetime") {
        constraints(nullable: "false")
      }
    }
  }

  changeSet(author: "Casey", id: "create-log-class-2") {
    addColumn(tableName: "chat") {
      column(name: "log_id", type: "bigint")
    }
  }

  changeSet(author: "Casey", id: "create-log-class-4") {
    createIndex(indexName: "FK_73ojs93ssk0yqcsdebbve1own", tableName: "chat") {
      column(name: "log_id")
    }
  }

  changeSet(author: "Casey", id: "create-log-class-3") {
    addForeignKeyConstraint(baseColumnNames: "log_id", baseTableName: "chat", constraintName: "FK_73ojs93ssk0yqcsdebbve1own", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "log", referencesUniqueColumn: "false")
  }
}
