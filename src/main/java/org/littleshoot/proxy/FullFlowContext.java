package org.littleshoot.proxy;

import org.littleshoot.proxy.impl.ClientToProxyConnection;
import org.littleshoot.proxy.impl.ProxyToServerConnection;
import org.littleshoot.proxy.impl.RouteId;

/**
 * Extension of {@link FlowContext} that provides additional information (which
 * we know after actually processing the request from the client).
 */
public class FullFlowContext extends FlowContext {
	private final RouteId routeId;
	private final ChainedProxy chainedProxy;

	public FullFlowContext(ClientToProxyConnection clientConnection, ProxyToServerConnection serverConnection) {
		super(clientConnection);
		this.routeId = serverConnection.getRouteId();
		this.chainedProxy = serverConnection.getChainedProxy();
	}

	/**
	 * The host and port for the server (i.e. the ultimate endpoint).
	 * 
	 * @return
	 */
	public RouteId getRouteId() {
		return routeId;
	}

	/**
	 * The chained proxy (if proxy chaining).
	 * 
	 * @return
	 */
	public ChainedProxy getChainedProxy() {
		return chainedProxy;
	}

}
