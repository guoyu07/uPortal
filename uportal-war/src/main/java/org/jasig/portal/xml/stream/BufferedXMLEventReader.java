/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.portal.xml.stream;

import java.util.LinkedList;
import java.util.ListIterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

/**
 * Buffers XML events for later re-reading
 * 
 * @author Eric Dalquist
 * @version $Revision$
 */
public class BufferedXMLEventReader extends BaseXMLEventReader {
    private final LinkedList<XMLEvent> eventBuffer = new LinkedList<XMLEvent>();
    private int eventLimit = 0;
    private ListIterator<XMLEvent> bufferReader = null;
    
    /**
     * Create new buffering reader, no buffering is done until {@link #mark(int)} is called.
     */
    public BufferedXMLEventReader(XMLEventReader reader) {
        super(reader);
    }
    
    /**
     * Create new buffering reader. Calls {@link #mark(int)} with the specified event limit
     * @see #mark(int)
     */
    public BufferedXMLEventReader(XMLEventReader reader, int eventLimit) {
        super(reader);
        this.eventLimit = eventLimit;
    }

    /* (non-Javadoc)
     * @see org.jasig.portal.xml.stream.BaseXMLEventReader#internalNextEvent()
     */
    @Override
    protected XMLEvent internalNextEvent() throws XMLStreamException {
        //If there is an iterator to read from reset was called, use the iterator
        //until it runs out of events.
        if (this.bufferReader != null) {
            final XMLEvent event = this.bufferReader.next();

            //If nothing left in the iterator, remove the reference and fall through to direct reading
            if (!this.bufferReader.hasNext()) {
                this.bufferReader = null;
            }
            
            return event;
        }

        //Get the next event from the underlying reader
        final XMLEvent event = this.getParent().nextEvent();
        
        //add the event to the buffer and trim off old events if the buffer has exceded the event limit 
        this.eventBuffer.offer(event);
        if (this.eventLimit >= 0 && this.eventBuffer.size() > this.eventLimit) {
            this.eventBuffer.poll();
        }
        
        return event;
    }
    
    @Override
    public boolean hasNext() {
        return this.bufferReader != null || super.hasNext();
    }
    
    @Override
    public XMLEvent peek() throws XMLStreamException {
        if (this.bufferReader != null) {
            final XMLEvent event = this.bufferReader.next();
            this.bufferReader.previous(); //move the iterator back
            return event;
        }
        return super.peek();
    }

    /**
     * Same as calling {@link #mark(int)} with -1.
     */
    public void mark() {
        this.mark(-1);
    }
    
    /**
     * Start buffering events
     * @param eventLimit the maximum number of events to buffer. -1 will buffer all events, 0 will buffer no events.
     */
    public void mark(int eventLimit) {
        this.eventLimit = eventLimit;
        this.eventBuffer.clear();
        this.bufferReader = null;
    }

    /**
     * Reset the reader to these start of the buffered events.
     */
    public void reset() {
        if (this.eventBuffer.isEmpty()) {
            this.bufferReader = null;
        }
        else {
            this.bufferReader = this.eventBuffer.listIterator();
        }
    }
    
    @Override
    public void close() throws XMLStreamException {
        this.mark(0);
        super.close();
    }

    /**
     * @return The number of events in the buffer.
     */
    public int bufferSize() {
        return this.eventBuffer.size();
    }

    /**
     * If reading from the buffer after a {@link #reset()} call an {@link IllegalStateException} will be thrown.
     */
    @Override
    public void remove() {
        if (this.bufferReader != null && this.bufferReader.hasNext()) {
            throw new IllegalStateException("Cannot remove a buffered element");
        }
        
        super.remove();
    }
}