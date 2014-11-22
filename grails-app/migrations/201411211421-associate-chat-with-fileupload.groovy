databaseChangeLog = {

	changeSet(author: "Casey (generated)", id: "1416684124298-1") {
		addColumn(tableName: "file_upload") {
			column(name: "chat_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Casey (generated)", id: "1416684124298-3") {
		createIndex(indexName: "FK_bb0jhr01afo0cxiiolxwuy1yr", tableName: "file_upload") {
			column(name: "chat_id")
		}
	}

	changeSet(author: "Casey (generated)", id: "1416684124298-2") {
		addForeignKeyConstraint(baseColumnNames: "chat_id", baseTableName: "file_upload", constraintName: "FK_bb0jhr01afo0cxiiolxwuy1yr", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "chat", referencesUniqueColumn: "false")
	}
}
