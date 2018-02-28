
package com.artigile.warehouse.webservices;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.artigile.warehouse.webservices package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetMarketProposalsByFilterResponse_QNAME = new QName("http://webservices.marketproposals.artigile.com/", "getMarketProposalsByFilterResponse");
    private final static QName _GetMarketProposalsByFilter_QNAME = new QName("http://webservices.marketproposals.artigile.com/", "getMarketProposalsByFilter");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.artigile.warehouse.webservices
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetMarketProposalsByFilterResponse }
     * 
     */
    public GetMarketProposalsByFilterResponse createGetMarketProposalsByFilterResponse() {
        return new GetMarketProposalsByFilterResponse();
    }

    /**
     * Create an instance of {@link MarketProposal }
     * 
     */
    public MarketProposal createMarketProposal() {
        return new MarketProposal();
    }

    /**
     * Create an instance of {@link MarketProposalFilter }
     * 
     */
    public MarketProposalFilter createMarketProposalFilter() {
        return new MarketProposalFilter();
    }

    /**
     * Create an instance of {@link MarketProposalSorting }
     * 
     */
    public MarketProposalSorting createMarketProposalSorting() {
        return new MarketProposalSorting();
    }

    /**
     * Create an instance of {@link GetMarketProposalsByFilter }
     * 
     */
    public GetMarketProposalsByFilter createGetMarketProposalsByFilter() {
        return new GetMarketProposalsByFilter();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMarketProposalsByFilterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.marketproposals.artigile.com/", name = "getMarketProposalsByFilterResponse")
    public JAXBElement<GetMarketProposalsByFilterResponse> createGetMarketProposalsByFilterResponse(GetMarketProposalsByFilterResponse value) {
        return new JAXBElement<GetMarketProposalsByFilterResponse>(_GetMarketProposalsByFilterResponse_QNAME, GetMarketProposalsByFilterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMarketProposalsByFilter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://webservices.marketproposals.artigile.com/", name = "getMarketProposalsByFilter")
    public JAXBElement<GetMarketProposalsByFilter> createGetMarketProposalsByFilter(GetMarketProposalsByFilter value) {
        return new JAXBElement<GetMarketProposalsByFilter>(_GetMarketProposalsByFilter_QNAME, GetMarketProposalsByFilter.class, null, value);
    }

}
