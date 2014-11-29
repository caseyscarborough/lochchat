<div class="modal fade" id="inviteUsersModal" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="inviteUsersModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title" id="inviteUsersModalLabel">Invite Users to this Chatroom</h4>
      </div>
      <div class="modal-body">
        <div class="form-group">
          <label for="chatroom-invitees">Email Addresses or Usernames</label>
          <input id="chatroom-invitees" class="form-control tagsinput" data-role="tagsinput" placeholder="Email Addresses or Usernames">
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-danger" data-dismiss="modal">Cancel</button>
        <button id="invite-users-button" type="button" class="btn btn-primary" data-loading-text="Inviting Users...">Invite Users</button>
      </div>
    </div>
  </div>
</div>