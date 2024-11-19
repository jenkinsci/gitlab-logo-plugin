window.addEventListener("DOMContentLoaded", () => {
    document.querySelector(".gll-clear-cache-button").addEventListener("click", (event) => {
        event.preventDefault();
        const { rootUrl } = event.target.dataset;

        fetch(`${rootURL}/descriptor/org.jenkinsci.plugins.gitlablogo.GitlabLogoProperty/clearCache`, {
            method: "post",
            headers: crumb.wrap({}),
        }).then((rsp) => {
            if (rsp.ok) {
                notificationBar.show("Clear cache done", notificationBar.SUCCESS);
            }
        });
    });
});
