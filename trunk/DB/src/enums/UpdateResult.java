package enums;

public enum UpdateResult {
	finish_download,
	finish_extract,
	start_update_table,
	finish_update_table,
	finish_delete,
	done,
	another_update_is_in_progress,
	cancel_accepted,
	exception;
}
