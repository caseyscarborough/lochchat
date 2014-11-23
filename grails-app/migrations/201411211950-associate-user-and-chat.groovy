databaseChangeLog = {

	changeSet(author: "casey (generated)", id: "1416703867910-1") {
		createTable(tableName: "user_chats") {
			column(name: "chat_id", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "user_id", type: "bigint") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "casey (generated)", id: "1416703867910-2") {
		addPrimaryKey(columnNames: "user_id, chat_id", tableName: "user_chats")
	}

	changeSet(author: "casey (generated)", id: "1416703867910-5") {
		createIndex(indexName: "FK_6sirymnurqp3xvlcope2w0jsk", tableName: "user_chats") {
			column(name: "chat_id")
		}
	}

	changeSet(author: "casey (generated)", id: "1416703867910-6") {
		createIndex(indexName: "FK_sng2052jwxq1auy9ijxrwsw9h", tableName: "user_chats") {
			column(name: "user_id")
		}
	}

	changeSet(author: "casey (generated)", id: "1416703867910-3") {
		addForeignKeyConstraint(baseColumnNames: "chat_id", baseTableName: "user_chats", constraintName: "FK_6sirymnurqp3xvlcope2w0jsk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "chat", referencesUniqueColumn: "false")
	}

	changeSet(author: "casey (generated)", id: "1416703867910-4") {
		addForeignKeyConstraint(baseColumnNames: "user_id", baseTableName: "user_chats", constraintName: "FK_sng2052jwxq1auy9ijxrwsw9h", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "user", referencesUniqueColumn: "false")
	}
}
