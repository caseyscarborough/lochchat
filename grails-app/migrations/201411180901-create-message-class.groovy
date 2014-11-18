databaseChangeLog = {

	changeSet(author: "Casey", id: "create-message-class-1") {
		createTable(tableName: "message") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "messagePK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "contents", type: "longtext") {
				constraints(nullable: "false")
			}

			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "log_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "user", type: "varchar(255)")
		}
	}

	changeSet(author: "Casey", id: "create-message-class-2") {
		addColumn(tableName: "log") {
			column(name: "date_created", type: "datetime") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Casey", id: "create-message-class-4") {
		createIndex(indexName: "FK_nm3pvylpl8eg5l0v3xs867g9i", tableName: "message") {
			column(name: "log_id")
		}
	}

	changeSet(author: "Casey", id: "create-message-class-5") {
		dropColumn(columnName: "contents", tableName: "log")
	}

	changeSet(author: "Casey", id: "create-message-class-3") {
		addForeignKeyConstraint(baseColumnNames: "log_id", baseTableName: "message", constraintName: "FK_nm3pvylpl8eg5l0v3xs867g9i", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "log", referencesUniqueColumn: "false")
	}
}
