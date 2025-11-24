# Getting Started

This service provides two ways to fetch GitHub issues: via a REST API and via the Model Context Protocol (MCP).

## Prerequisites

- Java 21+
- Maven
- A GitHub Personal Access Token (optional, but recommended to avoid rate limits)

## Running the Service

Start the service in development mode:

```bash
./mvnw quarkus:dev
```

The service will start on `http://localhost:8080`.

## REST API

The service exposes a REST endpoint to fetch issues.

**Endpoint:** `POST /api/issues/fetch`
**Content-Type:** `application/json`

### Request Body

```json
{
  "repository": "quarkusio/quarkus",
  "count": 5,
  "token": "YOUR_GITHUB_TOKEN" 
}
```

- `repository`: The GitHub repository in `owner/repo` format (required).
- `count`: The number of issues to fetch (required).
- `token`: Your GitHub Personal Access Token (optional).

### Response

The response is a JSON array of issues.

```json
[
  {
    "title": "Issue Title",
    "author": "author-username",
    "url": "https://github.com/owner/repo/issues/1",
    "description": "Full description of the issue...",
    "comments": [
      {
        "author": "commenter-username",
        "body": "Comment text...",
        "date": "2023-10-27T10:00:00Z"
      }
    ],
    "number": 1
  }
]
```

### Example

```bash
curl -X POST http://localhost:8080/api/issues/fetch \
  -H "Content-Type: application/json" \
  -d '{
    "repository": "quarkusio/quarkus",
    "count": 2
  }'
```

## MCP (Model Context Protocol)

This service implements the Model Context Protocol, allowing AI agents to use it as a tool.

### Tool: `getIssues`

Fetches issues from a GitHub repository and returns them as a JSON string.

**Arguments:**

- `repository` (string): The GitHub repository in `owner/repo` format.
- `count` (int): The number of issues to fetch.

**Usage:**

When connected to an MCP client (like an AI assistant), the tool will be available as `getIssues`. The agent can call this tool to retrieve issue data for analysis or summarization.

### MCP Transport

The MCP server runs over HTTP (SSE) on the same port as the REST API.
- SSE Endpoint: `http://localhost:8080/mcp/sse`
- POST Endpoint: `http://localhost:8080/mcp/messages`
