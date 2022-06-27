package com.empconn.response;

import java.util.HashSet;
import java.util.Set;

import com.empconn.dto.CheckListInformationDto;
import com.empconn.dto.ProjectInformationDto;
import com.empconn.dto.ResourceInformationDto;

public class MockResponse {
	public static ProjectInformationDto getMockProjectInformation() {
		final Set<ResourceInformationDto> resourceInformation = new HashSet<>();
		final ResourceInformationDto ri1 = new ResourceInformationDto("location-1", "title-1", 1, 100, null, null, "primarySkills-1");
		final ResourceInformationDto ri2 = new ResourceInformationDto("location-2", "title-2", 2, 200, null, null, "primarySkills-2");
		final ResourceInformationDto ri3 = new ResourceInformationDto("location-3", "title-3", 3, 300, null, null, "primarySkills-3");
		resourceInformation.add(ri1);
		resourceInformation.add(ri2);
		resourceInformation.add(ri3);



		final Set<CheckListInformationDto> checklist = new HashSet<>();
		final CheckListInformationDto cl1 = new CheckListInformationDto("description-1", "comments-1");
		final CheckListInformationDto cl2 = new CheckListInformationDto("description-2", "comments-2");
		final CheckListInformationDto cl3 = new CheckListInformationDto("description-3", "comments-3");
		checklist.add(cl1);
		checklist.add(cl2);
		checklist.add(cl3);


		return new ProjectInformationDto("projectName", "vertical", "subCategory", "customerName",
				"businessManagerName", "projectManagerName", "scope", null, null, null, resourceInformation,
				"initiatedBy", null, "approvedBy", null, checklist);
	}
}
