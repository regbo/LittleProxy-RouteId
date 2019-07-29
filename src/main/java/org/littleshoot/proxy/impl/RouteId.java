package org.littleshoot.proxy.impl;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.Generated;

import org.apache.commons.lang3.StringUtils;
import org.littleshoot.proxy.ChainedProxy;
import org.littleshoot.proxy.ChainedProxyManager;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;

public class RouteId {

	private final String hostAndPort;

	private final String proxyHostAndPort;

	@Generated("SparkTools")
	private RouteId(Builder builder) {
		this.hostAndPort = builder.hostAndPort;
		this.proxyHostAndPort = builder.proxyHostAndPort;
	}

	public static RouteId build(DefaultHttpProxyServer proxyServer, ClientDetails clientDetails,
			HttpRequest httpRequest) {
		String hostAndPort = ProxyUtils.parseHostAndPort(httpRequest);
		if (StringUtils.isBlank(hostAndPort)) {
			List<String> hosts = httpRequest.headers().getAll(HttpHeaders.Names.HOST);
			if (hosts != null && !hosts.isEmpty()) {
				hostAndPort = hosts.get(0);
			}
		}
		if (hostAndPort == null || hostAndPort.isEmpty())
			return null;
		InetSocketAddress inetSocketAddress = getProxyInetSocketAddress(proxyServer, clientDetails, httpRequest);
		if (inetSocketAddress == null)
			return RouteId.builder().withHostAndPort(hostAndPort).build();
		String proxyHostAndPort = String.format("%s:%s", inetSocketAddress.getHostString(),
				inetSocketAddress.getPort());
		return RouteId.builder().withHostAndPort(hostAndPort).withProxyHostAndPort(proxyHostAndPort).build();
	}

	private static InetSocketAddress getProxyInetSocketAddress(DefaultHttpProxyServer proxyServer,
			ClientDetails clientDetails, HttpRequest httpRequest) {
		ChainedProxyManager chainedProxyManager = proxyServer.getChainProxyManager();
		if (chainedProxyManager == null)
			return null;
		Queue<ChainedProxy> chainedProxies = new ConcurrentLinkedQueue<>();
		chainedProxyManager.lookupChainedProxies(httpRequest, chainedProxies, clientDetails);
		ChainedProxy chainedProxy = chainedProxies.peek();
		if (chainedProxy == null)
			return null;
		chainedProxy.filterRequest(httpRequest);
		return chainedProxy.getChainedProxyAddress();
	}

	/**
	 * Creates builder to build {@link RouteId}.
	 * 
	 * @return created builder
	 */
	@Generated("SparkTools")
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder to build {@link RouteId}.
	 */
	@Generated("SparkTools")
	public static final class Builder {
		private String hostAndPort;
		private String proxyHostAndPort;

		private Builder() {
		}

		public Builder withHostAndPort(String hostAndPort) {
			this.hostAndPort = hostAndPort;
			return this;
		}

		public Builder withProxyHostAndPort(String proxyHostAndPort) {
			this.proxyHostAndPort = proxyHostAndPort;
			return this;
		}

		public RouteId build() {
			return new RouteId(this);
		}
	}

	public String getHostAndPort() {
		return hostAndPort;
	}

	public String getProxyHostAndPort() {
		return proxyHostAndPort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostAndPort == null) ? 0 : hostAndPort.hashCode());
		result = prime * result + ((proxyHostAndPort == null) ? 0 : proxyHostAndPort.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteId other = (RouteId) obj;
		if (hostAndPort == null) {
			if (other.hostAndPort != null)
				return false;
		} else if (!hostAndPort.equals(other.hostAndPort))
			return false;
		if (proxyHostAndPort == null) {
			if (other.proxyHostAndPort != null)
				return false;
		} else if (!proxyHostAndPort.equals(other.proxyHostAndPort))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RouteId [hostAndPort=" + hostAndPort + ", proxyHostAndPort=" + proxyHostAndPort + "]";
	}
}
