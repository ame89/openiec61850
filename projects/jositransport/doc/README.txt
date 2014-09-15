/*
 * Copyright Fraunhofer ISE 2011
 *
 * This file is part of jOSITransport.
 * For more information visit http://www.openmuc.org
 *
 * jOSITransport is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jOSITransport is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jOSITransport.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
 
Authors:
Stefan Feuerhahn
Chau Do

jOSITransport is Java library implementing the OSI Transport Service Layer as specified by
ISO8072/X.214, ISO8073/X.224, and RFC 1006.
These layers sit on top of TCP/IP.

For the latest release of this software visit http://www.openmuc.org.

For an example on how to use jOSITransport see the sample/ folder.
To compile and execute the example on Linux use something like this:
javac -cp "../../../../build/lib/jositransport-<version>.jar" SampleServer.java SampleClient.java
java -cp "../../../../build/lib/jositransport-<version>.jar:./" SampleServer
java -cp "../../../../build/lib/jositransport-<version>.jar:./" SampleClient
