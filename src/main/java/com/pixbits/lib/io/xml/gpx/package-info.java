@XmlSchema(
  namespace = "http://www.topografix.com/GPX/1/1",
  elementFormDefault = XmlNsForm.QUALIFIED,
  xmlns = {
    @XmlNs(prefix= "", namespaceURI = "http://www.topografix.com/GPX/1/1"),
    @XmlNs(prefix= "gpxx", namespaceURI = "http://www.garmin.com/xmlschemas/GpxExtensions/v3"),
    @XmlNs(prefix= "gpxtpx", namespaceURI = "http://www.garmin.com/xmlschemas/TrackPointExtension/v2")
  }
)
package com.pixbits.lib.io.xml.gpx;

import jakarta.xml.bind.annotation.XmlNsForm;
import jakarta.xml.bind.annotation.XmlSchema;
import jakarta.xml.bind.annotation.XmlNs;
