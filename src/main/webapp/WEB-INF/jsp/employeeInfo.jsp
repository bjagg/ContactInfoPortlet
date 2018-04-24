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
    <h2 class="text-center"><spring:message code="page.employee.title"/></h2>

<c:choose>
<c:when test="${updateRequired}">
    <portlet:actionURL var="saveContactInfoUrl"/>
    <form id="${n}_edit_form" role="form" class="form-vertical" method="post" action="${saveContactInfoUrl}">
    <input type="hidden" name="username" value="${employeeInfo.username}"/>

    <div class="form-group emergency-phone text-center">
        <label for="emergency-phone"><spring:message code="employee-info.emergency-phone"/></label>
        <input type="text" class="form-control" name="emergencyPhone" value="${employeeInfo.emergencyPhone}"/>
    </div>

    <h3 class="font-weight-bold"><spring:message code="directory-info.section-title"/></h3>
    <fieldset>
    <div class="form-group title">
        <label for="title"><spring:message code="directory-info.title"/></label>
        <input type="text" class="form-control" name="title" value="${directoryInfo.title}"/>
    </div>
    <div class="form-group dept">
        <label for="dept"><spring:message code="directory-info.dept"/></label>
        <input type="text" class="form-control" name="dept" value="${directoryInfo.dept}"/>
    </div>
    <fieldset>
    <div class="form-group phone">
        <label for="phone"><spring:message code="directory-info.phone"/></label>
        <input type="text" class="form-control" name="phone" value="${directoryInfo.phone}"/>
    </div>
    <div class="form-group location">
        <label for="location"><spring:message code="directory-info.location"/></label>
        <input type="text" class="form-control" name="location" value="${directoryInfo.location}"/>
    </div>
    <div class="form-group use-dept-values">
        <label for="use-dept-values">
            <input type="checkbox" name="useDeptValues"  />
            <spring:message code="directory-info.use-dept-values"/>
        </label>
    </div>
    </fieldset>
    <div class="form-group hours">
        <label for="hours"><spring:message code="directory-info.hours"/></label>
        <input type="text" class="form-control" name="hours" value="${directoryInfo.hours}"/>
    </div>
    <div class="form-group fax">
        <label for="fax"><spring:message code="directory-info.fax"/></label>
        <input type="text" class="form-control" name="fax" value="${directoryInfo.fax}"/>
    </div>
    <div class="form-group supervisor">
        <label for="supervisor"><spring:message code="directory-info.supervisor"/></label>
        <input type="text" class="form-control" name="supervisor" value="${directoryInfo.supervisor}"/>
    </div>
    </fieldset>

    <h3 class="font-weight-bold"><spring:message code="employee-info.section-title"/></h3>
    <fieldset>
    <div class="form-group bio">
        <label for="bio"><spring:message code="employee-info.bio"/></label>
        <input type="textarea" class="form-control" name="bio" value="${employeeInfo.bio}"/>
    </div>
    <div class="form-group credentials">
        <label for="credentials"><spring:message code="employee-info.credentials"/></label>
        <input type="text" class="form-control" name="credentials" value="${employeeInfo.credentials}"/>
    </div>
    <div class="form-group url">
        <label for="url"><spring:message code="employee-info.url"/></label>
        <input type="text" class="form-control" name="url" value="${employeeInfo.personalUrl}"/>
    </div>
    </fieldset>

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

<portlet:resourceURL var="empInfo" id="emp-info"/>
<a href="${empInfo}"> this is a resource URL</a>

<!--
    <h3>Testing (debug info)</h3>
    <p><pre>${updateRequired}</pre></p>
    <p><pre>${employeeInfo}</pre></p>
    <p><pre>${employeeInfo}</pre></p>
-->
</div>

