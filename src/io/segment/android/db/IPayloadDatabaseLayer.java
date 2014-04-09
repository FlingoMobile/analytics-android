package io.segment.android.db;

import io.segment.android.models.BasePayload;
import io.segment.android.utils.IThreadedLayer;

import java.util.List;

/**
 * Handles communication with the database
 * on its own thread to achieve SQL thread safety.
 *
 */
public interface IPayloadDatabaseLayer extends IThreadedLayer {

	//
	// Callbacks
	// 
	
	/**
	 * Called when an enqueue completes.
	 *
	 */
	public interface EnqueueCallback {
		/**
		 * Called when an enqueue finishes.
		 * @param success Whether the enqueue was successful.
		 * @param count The new database size
		 */
		public void onEnqueue(boolean success, long rowCount);
	}
	
	/**
	 * Callback for when a database payload query returns
	 * @author ivolo
	 *
	 */
	public interface PayloadCallback {
		public void onPayload(long minId, long maxId, List<BasePayload> payloads);
	}
	
	/**
	 * Callback for when payloads are removed from the database
	 * @author ivolo
	 *
	 */
	public interface RemoveCallback {
		public void onRemoved(int removed);
	}
	
	//
	// Methods
	//
	
	/**
	 * Adds a payload to the database
	 * @param payload
	 * @param callback
	 */
	public void enqueue(BasePayload payload,
						EnqueueCallback callback);
	
	/**
	 * Gets and removes the next payload from the database
	 * @param callback
	 */
	public void removeNextPayload(PayloadCallback callback);
}
