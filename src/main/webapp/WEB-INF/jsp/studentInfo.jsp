<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<c:set var="n"><portlet:namespace/></c:set>

<div id="${n}container container">
    <h2 class="text-center"><spring:message code="page.title"/></h2>

<c:choose>
<c:when test="${updateRequired}">
    <portlet:actionURL var="saveContactInfoUrl"/>
    <form id="${n}_edit_form" role="form" class="form-vertical" method="post" action="${saveContactInfoUrl}">
    <input type="hidden" name="username" value="${contactInfo.username}"/>

    <h3 class="font-weight-bold"><spring:message code="contact-info.title"/></h3>
    <p class="small text-muted"><spring:message code="contact-info.description"/></p>
    <fieldset>
    <div class="form-group address">
        <label for="address"><spring:message code="contact-info.address"/></label>
        <input type="text" class="form-control" name="address" value="${contactInfo.address}"/>
    </div>
    <div class="form-group city">
        <label for="city"><spring:message code="contact-info.city"/></label>
        <input type="text" class="form-control" name="city" value="${contactInfo.city}"/>
    </div>
    <div class="form-group state">
        <label for="state"><spring:message code="contact-info.state"/></label>
        <input type="text" class="form-control" name="state" value="${contactInfo.state}"/>
    </div>
    <div class="form-group zip">
        <label for="zip"><spring:message code="contact-info.zip"/></label>
        <input type="text" class="form-control" name="zip" value="${contactInfo.zipCode}"/>
    </div>
    <div class="form-group county">
        <label for="county"><spring:message code="contact-info.county"/></label>
        <input type="text" class="form-control" name="county" value="${contactInfo.county}"/>
    </div>
    <div class="form-group phone">
        <label for="phone"><spring:message code="contact-info.phone"/></label>
        <input type="text" class="form-control" name="phoneNumber" value="${contactInfo.phoneNumber}"/>
        <label for="is-mobile">
            <spring:message code="contact-info.is-mobile"/>
            <input type="checkbox" name="is-mobile" ${contactInfo.getMobile() ? 'checked': ''} />
        </label>
    </div>
    <div class="checkbox is-mobile">
    </div>
    <div class="form-group alt-phone">
        <label for="alt-phone"><spring:message code="contact-info.alt-phone"/></label>
        <input type="text" class="form-control" name="altPhone" value="${contactInfo.altPhone}"/>
    </div>
    </fieldset>

    <c:if test="${showCommunicationPreferences}">
    <h3 class="font-weight-bold"><spring:message code="preferred-com.title"/></h3>
    <p class="small text-muted"><spring:message code="preferred-com.description"/></p>
    <fieldset>
    <div class="checkbox sms">
        <label for="sms">
            <input type="checkbox" name="sms" id="sms"/>
            <spring:message code="preferred-com.sms"/>
        </label>
    </div>
    <div class="checkbox mobile-app">
        <label for="mobile-app">
            <input type="checkbox" name="mobile-app" id="mobile-app"/>
            <spring:message code="preferred-com.mobile-app"/>
        </label>
    </div>
    <div class="checkbox portal">
        <label for="portal">
            <input type="checkbox" name="portal" id="portal"/>
            <spring:message code="preferred-com.portal"/>
        </label>
    </div>
    <div class="checkbox none">
        <label for="none">
            <input type="checkbox" name="none" id="none"/>
            <spring:message code="preferred-com.none"/>
        </label>
    </div>
    </fieldset>
    </c:if>

    <c:if test="${showEthnicity}">
    <h3 class="font-weight-bold"><spring:message code="ethnicity.title"/></h3>
    <p class="small text-muted"><spring:message code="ethnicity.description"/></p>
    <fieldset>
    <div class="checkbox american-native">
        <label for="american-native">
            <input type="checkbox" name="american-native" id="american-native"/>
            <spring:message code="ethnicity.american-native"/>
        </label>
    </div>
    <div class="checkbox asian">
        <label for="asian">
            <input type="checkbox" name="asian" id="asian"/>
            <spring:message code="ethnicity.asian"/>
        </label>
    </div>
    <div class="checkbox black">
        <label for="black">
            <input type="checkbox" name="black" id="black"/>
            <spring:message code="ethnicity.black"/>
        </label>
    </div>
    <div class="checkbox hispanic">
        <label for="hispanic">
            <input type="checkbox" name="hispanic" id="hispanic"/>
            <spring:message code="ethnicity.hispanic"/>
        </label>
    </div>
    <div class="checkbox pacific-islander">
        <label for="pacific-islander">
            <input type="checkbox" name="pacific-islander" id="pacific-islander"/>
            <spring:message code="ethnicity.pacific-islander"/>
        </label>
    </div>
    <div class="checkbox white">
        <label for="white">
            <input type="checkbox" name="white" id="white"/>
            <spring:message code="ethnicity.white"/>
        </label>
    </div>
    <div class="checkbox not-declared">
        <label for="not-declared">
            <input type="checkbox" name="not-declared" id="not-declared"/>
            <spring:message code="ethnicity.not-declared"/>
        </label>
    </div>
    </fieldset>
    </c:if>

    <div class="buttons text-center">
        <spring:message code="form.save" var="save"/>
        <input type="submit" value="${save}" class="btn-primary"/>
    </div>
    </form>
</c:when>
<c:otherwise>
<p>No update for contact information is required at this time.</p>
</c:otherwise>
</c:choose>

<!--
    <h3>Testing (debug info)</h3>
    <p><pre>${updateRequired}</pre></p>
    <p><pre>${contactInfo}</pre></p>
    <p><pre>${showCommunicationPreferences}</pre></p>
    <p><pre>${showEthnicity}</pre></p>
-->
</div>

