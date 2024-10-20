package com.libraryman_api.member;

public class UpdatePasswordDto {

    private String currentPassword;
    
    private String newPassword;

    public UpdatePasswordDto(String currentPassword, String newPassword) {
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}

    public UpdatePasswordDto() {
    }
    
    public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	@Override
    public String toString() {
        return "UpdatePasswordDto{" +
        "currentPassword='" + currentPassword + '\'' +
        ", newPassword='" + newPassword + '\'' +
        '}';
    }
}
