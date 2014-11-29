databaseChangeLog = {

	changeSet(author: "casey (generated)", id: "1417284105595-1") {
		createTable(tableName: "notification") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "notificationPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "is_dismissed", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "is_viewed", type: "bit") {
				constraints(nullable: "false")
			}

			column(name: "message", type: "longtext") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "casey (generated)", id: "1417284105595-3") {
		createIndex(indexName: "FK_1urdwwsh2ti15ta6f6p5dbdcp", tableName: "notification") {
			column(name: "user_id")
		}
	}

	changeSet(author: "casey (generated)", id: "1417284105595-2") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "notification", constraintName: "FK_1urdwwsh2ti15ta6f6p5dbdcp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}
}
