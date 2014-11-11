databaseChangeLog = {

	changeSet(author: "Casey", id: "create-chat-class-1") {
		createTable(tableName: "chat") {
			column(autoIncrement: "true", name: "id", type: "bigint") {
				constraints(nullable: "false", primaryKey: "true", primaryKeyName: "chatPK")
			}

			column(name: "version", type: "bigint") {
				constraints(nullable: "false")
			}

			column(name: "anonymous_users", type: "tinyblob")

			column(name: "end_time", type: "datetime")

			column(name: "start_time", type: "datetime") {
				constraints(nullable: "false")
			}

			column(name: "unique_id", type: "varchar(255)") {
				constraints(nullable: "false")
			}
		}
	}

	changeSet(author: "Casey", id: "create-chat-class-2") {
		createIndex(indexName: "unique_id_uniq_1415735948764", tableName: "chat", unique: "true") {
			column(name: "unique_id")
		}
	}
}
